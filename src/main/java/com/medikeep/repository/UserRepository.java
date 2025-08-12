package com.medikeep.repository;

import com.medikeep.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 이메일로 사용자를 조회하기 위한 메서드 (로그인, 중복 체크 등에 사용)
    Optional<User> findByEmail(String email);
}
