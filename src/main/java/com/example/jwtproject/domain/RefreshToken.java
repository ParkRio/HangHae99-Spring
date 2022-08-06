package com.example.jwtproject.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "refresh_token")
public class RefreshToken extends Timestamped {

    @Id
    @Column(nullable = false)
    private Long id;

    // Todo :: member_id 를 fk 로 가지고 있음, 연관관계의 주인
    @JoinColumn(name = "member_id", nullable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(nullable = false)
    private String value;

    public void updateValue(String token) {
        this.value = token;
    }
}
