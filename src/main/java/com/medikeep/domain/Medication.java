package com.medikeep.domain;

import com.medikeep.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Medication extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medication_id")
    private Long id;

    @Column(nullable = false)
    private String medicationName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MedicationType medicationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public Medication(String medicationName, MedicationType medicationType, User user) {
        this.medicationName = medicationName;
        this.medicationType = medicationType;
        this.user = user;
    }
}
