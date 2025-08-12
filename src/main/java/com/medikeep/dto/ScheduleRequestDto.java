package com.medikeep.dto;

import com.medikeep.domain.MedicationType;
import com.medikeep.domain.TimeSlot;
import com.medikeep.domain.Weekday;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

// 스케줄 생성/수정 요청 DTO
@Getter
@NoArgsConstructor
public class ScheduleRequestDto {
    private String medicationName;
    private MedicationType medicationType;
    private List<Weekday> weekdays;
    private List<TimeSlot> timeSlots;
}