package com.medikeep.repository;

import com.medikeep.domain.IntakeLog;
import com.medikeep.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface IntakeLogRepository extends JpaRepository<IntakeLog, Long> {
    List<IntakeLog> findByUserAndIntakeDate(User user, LocalDate date);

    @Query("SELECT DISTINCT i.intakeDate FROM IntakeLog i WHERE i.user = :user AND i.intakeDate BETWEEN :startDate AND :endDate")
    List<LocalDate> findDistinctIntakeDatesByUserAndMonth(
            @Param("user") User user,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    List<IntakeLog> findByUserAndIntakeDateBetween(User user, LocalDate startDate, LocalDate endDate);
}
