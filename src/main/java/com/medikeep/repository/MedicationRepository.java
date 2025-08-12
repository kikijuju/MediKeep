package com.medikeep.repository;

import com.medikeep.domain.Medication;
import com.medikeep.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface MedicationRepository extends JpaRepository<Medication, Long> {
    // 사용자와 약 이름으로 약 정보 조회
    Optional<Medication> findByMedicationNameAndUser(String medicationName, User user);
    List<Medication> findAllByUser(User user);
}
