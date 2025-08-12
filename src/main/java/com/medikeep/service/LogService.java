package com.medikeep.service;

import com.medikeep.domain.IntakeLog;
import com.medikeep.domain.IntakeSchedule;
import com.medikeep.domain.User;
import com.medikeep.domain.Weekday;
import com.medikeep.dto.LogCheckRequestDto;
import com.medikeep.dto.LogResponseDto;
import com.medikeep.dto.MonthlyLogResponseDto;
import com.medikeep.repository.IntakeLogRepository;
import com.medikeep.repository.IntakeScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LogService {

    private final IntakeLogRepository intakeLogRepository;
    private final IntakeScheduleRepository intakeScheduleRepository;

    @Transactional
    public void checkIntake(LogCheckRequestDto requestDto, User user) {
        LocalDate today = LocalDate.now();
        String todayName = today.getDayOfWeek().name().substring(0, 3);
        Weekday todayWeekday = Weekday.valueOf(todayName);

        // 1. 오늘, 해당 시간대에 복용해야 할 모든 스케줄을 찾음
        List<IntakeSchedule> schedulesToCheck = intakeScheduleRepository.findByUserAndWeekday(user, todayWeekday)
                .stream()
                .filter(schedule -> schedule.getTimeSlot().equals(requestDto.getTimeSlot()))
                .toList();

        if (schedulesToCheck.isEmpty()) {
            throw new IllegalArgumentException("해당 시간대에 복용할 약이 없습니다.");
        }

        // 2. 각 스케줄에 대해 복용 기록(Log) 생성
        for (IntakeSchedule schedule : schedulesToCheck) {
            // 이미 기록이 있는지 확인하여 중복 생성 방지
            boolean alreadyExists = intakeLogRepository.findByUserAndIntakeDate(user, today).stream()
                    .anyMatch(log -> log.getMedication().equals(schedule.getMedication()) && log.getTimeSlot().equals(schedule.getTimeSlot()));

            if (!alreadyExists) {
                IntakeLog log = IntakeLog.builder()
                        .intakeDate(today)
                        .timeSlot(schedule.getTimeSlot())
                        .taken(true)
                        .user(user)
                        .medication(schedule.getMedication())
                        .build();
                intakeLogRepository.save(log);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<LogResponseDto> getDailyLogs(LocalDate date, User user) {
        return intakeLogRepository.findByUserAndIntakeDate(user, date).stream()
                .map(LogResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MonthlyLogResponseDto getMonthlyLogs(int year, int month, User user) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        List<LocalDate> takenDates = intakeLogRepository.findDistinctIntakeDatesByUserAndMonth(user, startDate, endDate);
        return new MonthlyLogResponseDto(takenDates);
    }
}
