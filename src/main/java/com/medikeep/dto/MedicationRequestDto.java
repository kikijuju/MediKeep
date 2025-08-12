package com.medikeep.dto;


import com.medikeep.domain.MedicationType;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 약/영양제 생성 요청 DTO
@Getter
@NoArgsConstructor
public class MedicationRequestDto {
    private String medicationName;
    private MedicationType medicationType;
}