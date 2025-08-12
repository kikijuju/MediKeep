package com.medikeep.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeviceStatusUpdateRequestDto {
    private Long userId;        // 어떤 사용자의 장치인지 식별
    private Double temperature;   // 온도
    private Double humidity;      // 습도
    private Boolean hasMedication; // 약 유무
}
