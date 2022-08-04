package com.sparta.week04.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Member extends Timestamped{

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @JsonIgnore
    @Column(nullable = false)
    private String userPassword;

    @Transient
    @Column(nullable = false)
    private String userPasswordConfirm;

    @Column
    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Builder
    public Member(String userNickname, String userPassword, String userPasswordConfirm, Authority authority) {
        this.name = userNickname;
        this.userPassword = userPassword;
        this.userPasswordConfirm = userPasswordConfirm;
        this.authority = authority;
    }
}
