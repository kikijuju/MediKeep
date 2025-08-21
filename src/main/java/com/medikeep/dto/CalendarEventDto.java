package com.medikeep.dto;

import com.medikeep.domain.IntakeLog;
import com.medikeep.domain.IntakeSchedule;
import com.medikeep.domain.MedicationType;
import com.medikeep.domain.TimeSlot;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

// MyPage 달력에 표시될 개별 이벤트 정보 DTO
@Getter
@Builder
public class CalendarEventDto {
    private LocalDate date;
    private Long medicationId;
    private String medicationName;
    private MedicationType medicationType;
    private TimeSlot timeSlot;
    private boolean isTaken;
    private LocalDateTime confirmationTime; // 복용 확인 시간
}
