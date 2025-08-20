package com.medikeep.dto;

import com.medikeep.domain.EnvLog;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class EnvLogResponseDto {
    private Long id;
    private Double temperature;
    private Double humidity;
    private LocalDateTime createdAt;

    public static EnvLogResponseDto from(EnvLog e) {
        return EnvLogResponseDto.builder()
                .id(e.getId())
                .temperature(e.getTemperature())
                .humidity(e.getHumidity())
                .createdAt(e.getCreatedAt())
                .build();
    }
}