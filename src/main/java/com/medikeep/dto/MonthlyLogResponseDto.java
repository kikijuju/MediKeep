package com.medikeep.dto;

import lombok.Getter;
import java.time.LocalDate;
import java.util.List;

// 월별 복용 기록 조회 응답 DTO (달력용)
@Getter
public class MonthlyLogResponseDto {
    private List<LocalDate> takenDates; // 해당 월에 한 번이라도 복용한 날짜 목록

    public MonthlyLogResponseDto(List<LocalDate> takenDates) {
        this.takenDates = takenDates;
    }
}
