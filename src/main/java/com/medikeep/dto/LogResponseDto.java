package com.medikeep.dto;


import com.medikeep.domain.IntakeLog;
import com.medikeep.domain.TimeSlot;
import lombok.Getter;

// 일별 복용 기록 조회 응답 DTO
@Getter
public class LogResponseDto {
    private String medicationName;
    private TimeSlot timeSlot;
    private boolean isTaken;

    public LogResponseDto(IntakeLog log) {
        this.medicationName = log.getMedication().getMedicationName();
        this.timeSlot = log.getTimeSlot();
        this.isTaken = log.getTaken();
    }
}