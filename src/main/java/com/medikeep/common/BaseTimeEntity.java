package com.medikeep.common;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass // 이 클래스를 상속하는 엔티티들은 아래 필드들을 컬럼으로 인식하게 됩니다.
@EntityListeners(AuditingEntityListener.class) // Auditing(자동 시간 관리) 기능을 활성화합니다.
public abstract class BaseTimeEntity {

    @CreatedDate // 엔티티가 생성될 때 시간이 자동으로 저장됩니다.
    private LocalDateTime createdAt;

    @LastModifiedDate // 엔티티가 수정될 때 시간이 자동으로 저장됩니다.
    private LocalDateTime updatedAt;
}
