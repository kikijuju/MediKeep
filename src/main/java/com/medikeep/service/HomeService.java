package com.medikeep.service;

import com.medikeep.domain.*;
import com.medikeep.dto.DashboardResponseDto;
import com.medikeep.dto.DoseStatusDto;
import com.medikeep.repository.DeviceStatusRepository;
import com.medikeep.repository.IntakeLogRepository;
import com.medikeep.repository.IntakeScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final IntakeScheduleRepository intakeScheduleRepository;
    private final IntakeLogRepository intakeLogRepository;
    private final DeviceStatusRepository deviceStatusRepository;

    @Transactional(readOnly = true)
    public DashboardResponseDto getDashboardInfo(User user) {
        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        Weekday weekday = Weekday.valueOf(dayOfWeek.name().substring(0, 3)); // e.g., MONDAY -> MON

        // 1. 오늘 복용해야 할 스케줄 조회
        List<IntakeSchedule> todaySchedules = intakeScheduleRepository.findByUserAndWeekday(user, weekday);
        int totalDoses = todaySchedules.size();

        // 2. 오늘 복용한 기록 조회
        List<IntakeLog> todayLogs = intakeLogRepository.findByUserAndIntakeDate(user, today);
        int takenDoses = todayLogs.size();

        // 3. 시간대별 복용 완료 여부 맵 생성
        Map<TimeSlot, Boolean> takenStatusMap = todayLogs.stream()
                .collect(Collectors.toMap(IntakeLog::getTimeSlot, log -> true, (a, b) -> a));

        List<DoseStatusDto> doseStatusList = todaySchedules.stream()
                .map(schedule -> DoseStatusDto.builder()
                        .timeSlot(schedule.getTimeSlot())
                        .isTaken(takenStatusMap.getOrDefault(schedule.getTimeSlot(), false))
                        .build())
                .distinct() // 중복된 시간대 제거
                .collect(Collectors.toList());

        // 4. 장치 상태 조회
        Optional<DeviceStatus> deviceStatusOpt = deviceStatusRepository.findByUser(user);

        Double temperature = deviceStatusOpt.map(DeviceStatus::getTemperature).orElse(null);
        Double humidity = deviceStatusOpt.map(DeviceStatus::getHumidity).orElse(null);

        return DashboardResponseDto.builder()
                .totalDoses(totalDoses)
                .takenDoses(takenDoses)
                .isConnected(true) // TODO: 실제 블루투스 연결 상태 로직 추가 필요
                .temperature(temperature)
                .humidity(humidity)
                .doseStatusList(doseStatusList)
                .build();
    }
}
