package com.medikeep.dto;

import com.medikeep.domain.IntakeSchedule;
import com.medikeep.domain.TimeSlot;
import com.medikeep.domain.Weekday;
import lombok.Getter;

// 스케줄 조회 응답 DTO
@Getter
public class ScheduleResponseDto {
    private Long scheduleId;
    private String medicationName;
    private Weekday weekday;
    private TimeSlot timeSlot;

    public ScheduleResponseDto(IntakeSchedule schedule) {
        this.scheduleId = schedule.getId();
        this.medicationName = schedule.getMedication().getMedicationName();
        this.weekday = schedule.getWeekday();
        this.timeSlot = schedule.getTimeSlot();
    }
}