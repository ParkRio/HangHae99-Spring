package com.example.jwtproject.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

// Todo :: Main 클래스에 @EnableJpaAuditing 어노테이션을 추가

@Getter
@MappedSuperclass // Todo :: 공통 매핑 정보가 필요할 때, 부모 클래스에 선언하고 속성만 상속 받아서 사용하고 싶을 때 @MappedSuperclass를 사용한다.
@EntityListeners(AuditingEntityListener.class) // Todo :: Spring Data JPA에서 시간에 대해서 자동으로 값을 넣어주는 기능입니다.
public abstract class Timestamped {

    @CreatedDate
    private LocalDateTime createAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

}
