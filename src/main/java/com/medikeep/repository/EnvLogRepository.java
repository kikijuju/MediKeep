package com.medikeep.repository;

import com.medikeep.domain.EnvLog;
import com.medikeep.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EnvLogRepository extends JpaRepository<EnvLog, Long> {
    Optional<EnvLog> findTopByUserOrderByCreatedAtDesc(User user);

    Page<EnvLog> findAllByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    List<EnvLog> findAllByUserAndCreatedAtBetweenOrderByCreatedAtDesc(
            User user, LocalDateTime start, LocalDateTime end);

}
