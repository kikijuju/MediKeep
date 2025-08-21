package com.medikeep.service;

import com.medikeep.domain.IntakeLog;
import com.medikeep.domain.IntakeSchedule;
import com.medikeep.domain.User;
import com.medikeep.dto.CalendarEventDto;
import com.medikeep.repository.IntakeLogRepository;
import com.medikeep.repository.IntakeScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final IntakeScheduleRepository intakeScheduleRepository;
    private final IntakeLogRepository intakeLogRepository;

    @Transactional(readOnly = true)
    public List<CalendarEventDto> getCalendarEvents(int year, int month, User user) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        // 1. 해당 월의 모든 스케줄 조회
        List<IntakeSchedule> allSchedules = intakeScheduleRepository.findByUser(user);

        // 2. 해당 월의 모든 복용 기록 조회
        List<IntakeLog> monthlyLogs = intakeLogRepository.findByUserAndIntakeDateBetween(user, startDate, endDate);
        Map<String, IntakeLog> logMap = monthlyLogs.stream()
                .collect(Collectors.toMap(
                        log -> log.getIntakeDate() + ":" + log.getMedication().getId() + ":" + log.getTimeSlot(),
                        log -> log
                ));

        List<CalendarEventDto> events = new ArrayList<>();

        // 3. 해당 월의 모든 날짜를 순회하며 스케줄과 기록을 조합
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            String dayName = date.getDayOfWeek().name().substring(0, 3);
            for (IntakeSchedule schedule : allSchedules) {
                if (schedule.getWeekday().name().equals(dayName)) {
                    String key = date + ":" + schedule.getMedication().getId() + ":" + schedule.getTimeSlot();
                    IntakeLog log = logMap.get(key);

                    events.add(CalendarEventDto.builder()
                            .date(date)
                            .medicationId(schedule.getMedication().getId())
                            .medicationName(schedule.getMedication().getMedicationName())
                            .medicationType(schedule.getMedication().getMedicationType())
                            .timeSlot(schedule.getTimeSlot())
                            .isTaken(log != null)
                            .confirmationTime(log != null ? log.getCreatedAt() : null)
                            .build());
                }
            }
        }
        return events;
    }
}
