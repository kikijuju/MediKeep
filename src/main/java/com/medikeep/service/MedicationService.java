package com.medikeep.service;

import com.medikeep.domain.Medication;
import com.medikeep.domain.User;
import com.medikeep.dto.MedicationRequestDto;
import com.medikeep.dto.MedicationResponseDto;
import com.medikeep.repository.IntakeScheduleRepository;
import com.medikeep.repository.MedicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicationService {

    private final MedicationRepository medicationRepository;
    private final IntakeScheduleRepository intakeScheduleRepository;

    @Transactional
    public void createMedication(MedicationRequestDto requestDto, User user) {
        // 동일한 이름의 약이 이미 등록되었는지 확인
        medicationRepository.findByMedicationNameAndUser(requestDto.getMedicationName(), user)
                .ifPresent(m -> {
                    throw new IllegalArgumentException("이미 등록된 약입니다.");
                });

        Medication medication = Medication.builder()
                .medicationName(requestDto.getMedicationName())
                .medicationType(requestDto.getMedicationType())
                .user(user)
                .build();

        medicationRepository.save(medication);
    }

    @Transactional(readOnly = true)
    public List<MedicationResponseDto> getMedications(User user) {
        return medicationRepository.findAllByUser(user).stream()
                .map(MedicationResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteMedication(Long medicationId, User user) {
        Medication medication = medicationRepository.findById(medicationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 약입니다."));

        // 본인이 등록한 약이 맞는지 확인
        if (!medication.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("본인이 등록한 약만 삭제할 수 있습니다.");
        }

        // 1. 이 약을 사용하는 모든 스케줄을 먼저 삭제
        intakeScheduleRepository.deleteAllByMedication(medication);

        // 2. 약 정보 삭제
        medicationRepository.delete(medication);
    }
}
