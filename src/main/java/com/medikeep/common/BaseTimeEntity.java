package com.medikeep.common;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass // 이 클래스를 상속하는 엔티티들은 아래 필드들을 컬럼으로 인식
@EntityListeners(AuditingEntityListener.class) // Auditing(자동 시간 관리) 기능 활성화
public abstract class BaseTimeEntity {

    @CreatedDate // 엔티티 생성시 시간 자동 저장
    private LocalDateTime createdAt;

    @LastModifiedDate // 엔티티가 수정시 시간 자동 저장
    private LocalDateTime updatedAt;
}
