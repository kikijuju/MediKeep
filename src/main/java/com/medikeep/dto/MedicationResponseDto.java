package com.medikeep.dto;

import com.medikeep.domain.Medication;
import com.medikeep.domain.MedicationType;
import lombok.Getter;

// 약/영양제 조회 응답 DTO
@Getter
public class MedicationResponseDto {
    private Long medicationId;
    private String medicationName;
    private MedicationType medicationType;

    public MedicationResponseDto(Medication medication) {
        this.medicationId = medication.getId();
        this.medicationName = medication.getMedicationName();
        this.medicationType = medication.getMedicationType();
    }
}
