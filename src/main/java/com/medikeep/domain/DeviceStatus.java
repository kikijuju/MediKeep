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
    private Boolean isPillPresent;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @Builder
    public DeviceStatus(Double temperature, Double humidity, Boolean isPillPresent, User user) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.isPillPresent = isPillPresent;
        this.user = user;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateStatus(Double temperature, Double humidity, Boolean isPillPresent) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.isPillPresent = isPillPresent;
        this.updatedAt = LocalDateTime.now();
    }
}
