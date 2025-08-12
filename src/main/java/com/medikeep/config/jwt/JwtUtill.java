package com.medikeep.config.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtill {

    private final SecretKey secretKey;
    private final long expirationTime;

    public JwtUtill(
            @Value("${jwt.secret.key}") String secret,
            @Value("${jwt.expiration.time:86400000}") long expirationTime // 만료 시간 (기본값: 24시간)
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationTime = expirationTime;
    }

    // 이메일을 기반으로 JWT 생성
    public String createToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .subject(email) // 토큰의 주체 (사용자 이메일)
                .issuedAt(now) // 발급 시간
                .expiration(expiryDate) // 만료 시간
                .signWith(secretKey) // 비밀 키로 서명
                .compact();
    }
    // 토큰에서 이메일 추출
    public String getEmailFromToken(String token) {
        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token).getPayload().getSubject();
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            // 유효하지 않은 토큰 (만료, 변조 등)
            return false;
        }
    }
}
