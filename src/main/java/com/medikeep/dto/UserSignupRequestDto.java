package com.medikeep.dto;

import com.medikeep.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class UserSignupRequestDto {

    private String email;
    private String password;
    private String name;
    private LocalDate birthday;

    // DTO를 User 엔티티로 변환하는 메서드 (비밀번호 암호화 포함)
    public User toEntity(PasswordEncoder passwordEncoder) {
        return User.builder()
                .email(this.email)
                .password(passwordEncoder.encode(this.password)) // 비밀번호를 암호화하여 저장
                .name(this.name)
                .birthday(this.birthday)
                .build();
    }
}
