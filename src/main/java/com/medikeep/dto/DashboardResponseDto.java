package com.medikeep.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

// 홈 화면 전체 데이터를 담는 DTO
@Getter
@Builder
public class DashboardResponseDto {
    private int totalDoses;       // 오늘 복용해야 할 총 횟수
    private int takenDoses;       // 오늘 복용한 횟수
    private boolean isConnected;  // 블루투스 연결 여부 (임시로 true)
    private Double temperature;   // 현재 온도
    private Double humidity;      // 현재 습도
    private List<DoseStatusDto> doseStatusList; // 시간대별 복용 상태
}