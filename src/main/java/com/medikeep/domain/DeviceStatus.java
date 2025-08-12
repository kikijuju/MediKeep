package com.medikeep.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeviceStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double temperature;
    private Double humidity;
    private Boolean hasMedication;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @Builder
    public DeviceStatus(Double temperature, Double humidity, Boolean hasMedication, User user) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.hasMedication = hasMedication;
        this.user = user;
        this.updatedAt = LocalDateTime.now();
    }

    // 상태 업데이트 메서드 수정
    public void updateStatus(Double temperature, Double humidity, Boolean hasMedication) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.hasMedication = hasMedication;
        this.updatedAt = LocalDateTime.now();
    }
}
