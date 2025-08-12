package com.medikeep.repository;

import com.medikeep.domain.IntakeSchedule;
import com.medikeep.domain.Medication;
import com.medikeep.domain.User;
import com.medikeep.domain.Weekday;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IntakeScheduleRepository extends JpaRepository<IntakeSchedule, Long> {
    // 사용자와 요일로 스케줄 목록 조회
    List<IntakeSchedule> findByUserAndWeekday(User user, Weekday weekday);
    List<IntakeSchedule> findByUser(User user);
    // 특정 약에 해당하는 모든 스케줄 삭제
    void deleteAllByMedication(Medication medication);

}
