package com.medikeep.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeviceStatusUpdateRequestDto {
    private Long userId;
    private Double temperature;
    private Double humidity;
    private Boolean isPillPresent;
}