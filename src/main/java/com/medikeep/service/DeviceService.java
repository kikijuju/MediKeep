package com.medikeep.service;

import com.medikeep.domain.*;
import com.medikeep.dto.DeviceStatusUpdateRequestDto;
import com.medikeep.repository.DeviceStatusRepository;
import com.medikeep.repository.IntakeLogRepository;
import com.medikeep.repository.IntakeScheduleRepository;
import com.medikeep.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceStatusRepository deviceStatusRepository;
    private final UserRepository userRepository;
    private final IntakeScheduleRepository intakeScheduleRepository;
    private final IntakeLogRepository intakeLogRepository;

    @Transactional
    public void updateDeviceStatusAndLogIntake(DeviceStatusUpdateRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다. ID: " + requestDto.getUserId()));

        DeviceStatus currentStatus = deviceStatusRepository.findByUser(user).orElse(null);

        // 이전 상태가 '약 있음'(true)이고, 현재 상태가 '약 없음'(false)일 때 복용으로 간주
        boolean pillWasPresent = currentStatus != null && Boolean.TRUE.equals(currentStatus.getIsPillPresent());
        boolean pillIsAbsentNow = Boolean.FALSE.equals(requestDto.getIsPillPresent());

        if (pillWasPresent && pillIsAbsentNow) {
            autoCreateIntakeLog(user);
        }

        // 장치 상태 정보 업데이트 또는 새로 생성
        if (currentStatus != null) {
            currentStatus.updateStatus(requestDto.getTemperature(), requestDto.getHumidity(), requestDto.getIsPillPresent());
        } else {
            DeviceStatus newDeviceStatus = DeviceStatus.builder()
                    .user(user)
                    .temperature(requestDto.getTemperature())
                    .humidity(requestDto.getHumidity())
                    .isPillPresent(requestDto.getIsPillPresent())
                    .build();
            deviceStatusRepository.save(newDeviceStatus);
        }
    }

    private void autoCreateIntakeLog(User user) {
        LocalDate today = LocalDate.now();
        TimeSlot currentTimeSlot = determineCurrentTimeSlot();
        String todayName = today.getDayOfWeek().name().substring(0, 3);
        Weekday todayWeekday = Weekday.valueOf(todayName);

        // 현재 시간대에 복용해야 할 스케줄 조회
        List<IntakeSchedule> schedulesToLog = intakeScheduleRepository.findByUserAndWeekday(user, todayWeekday)
                .stream()
                .filter(schedule -> schedule.getTimeSlot().equals(currentTimeSlot))
                .toList();

        // 각 스케줄에 대해 복용 기록 생성
        for (IntakeSchedule schedule : schedulesToLog) {
            boolean alreadyExists = intakeLogRepository.findByUserAndIntakeDate(user, today).stream()
                    .anyMatch(log -> log.getMedication().equals(schedule.getMedication()) && log.getTimeSlot().equals(schedule.getTimeSlot()));

            if (!alreadyExists) {
                IntakeLog log = IntakeLog.builder()
                        .intakeDate(today)
                        .timeSlot(currentTimeSlot)
                        .taken(true)
                        .user(user)
                        .medication(schedule.getMedication())
                        .build();
                intakeLogRepository.save(log);
            }
        }
    }

    // 현재 시간에 따라 시간대(TimeSlot) 결정
    private TimeSlot determineCurrentTimeSlot() {
        int hour = LocalTime.now().getHour();
        if (hour >= 6 && hour < 12) return TimeSlot.MORNING;
        if (hour >= 12 && hour < 18) return TimeSlot.LUNCH; // Figma 디자인의 Afternoon을 Lunch로 간주
        if (hour >= 18 && hour < 22) return TimeSlot.EVENING;
        return TimeSlot.NIGHT;
    }
}
