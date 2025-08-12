// DeviceStatusRepository.java
package com.medikeep.repository;

import com.medikeep.domain.DeviceStatus;
import com.medikeep.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeviceStatusRepository extends JpaRepository<DeviceStatus, Long> {
    // 사용자로 장치 상태 조회
    Optional<DeviceStatus> findByUser(User user);
}