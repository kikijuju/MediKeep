package com.medikeep.dto;

import com.medikeep.domain.TimeSlot;
import lombok.Getter;
import lombok.NoArgsConstructor;


// 복용 완료 체크 요청 DTO
@Getter
@NoArgsConstructor
public class LogCheckRequestDto {
    private TimeSlot timeSlot; // 사용자가 체크한 시간대 (e.g., MORNING)
}