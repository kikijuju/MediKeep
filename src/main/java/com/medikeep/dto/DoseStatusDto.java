package com.medikeep.dto;

import com.medikeep.domain.TimeSlot;
import lombok.Builder;
import lombok.Getter;

// 시간대별 복용 상태를 담는 DTO
@Getter
@Builder
public class DoseStatusDto {
    private TimeSlot timeSlot; // MORNING, LUNCH, EVENING, NIGHT
    private boolean isTaken;   // 복용 완료 여부
}
