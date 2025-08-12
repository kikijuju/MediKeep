package com.medikeep.service;

import com.medikeep.domain.User;
import com.medikeep.dto.UserLoginRequestDto;
import com.medikeep.config.jwt.JwtUtill;
import com.medikeep.dto.UserSignupRequestDto;
import com.medikeep.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor // final 필드에 대한 생성자를 자동으로 만들어줍니다.
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtill jwtUtill;


    @Transactional
    public Long signup(UserSignupRequestDto requestDto) {
        // 이메일 중복 확인
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }
        // DTO를 엔티티로 변환 (비밀번호 암호화 포함)
        User user = requestDto.toEntity(passwordEncoder);
        // 사용자 저장
        User savedUser = userRepository.save(user);

        return savedUser.getId();
    }

    @Transactional(readOnly = true)
    public String login(UserLoginRequestDto requestDto) {
        // 1. 이메일로 사용자 조회
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));

        // 2. 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 3. JWT 생성 및 반환
        return jwtUtill.createToken(user.getEmail());
    }
}
