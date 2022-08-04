package com.sparta.week04.memberdto;

import com.sparta.week04.model.Authority;
import com.sparta.week04.model.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.intellij.lang.annotations.Pattern;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequestDto {
    @Pattern("^(?=.*\\d)(?=.*[a-zA-Z])[0-9a-zA-Z]{3,12}$")
    private String userNickname;

    @Pattern("^(?=.*\\d)(?=.*[a-zA-Z])[0-9a-zA-Z]{3,12}$")
    private String userPassword;

    @Pattern("^(?=.*\\d)(?=.*[a-zA-Z])[0-9a-zA-Z]{3,12}$")
    private String userPasswordConfirm;

    public Member toMember(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .userNickname(userNickname)
                .userPassword(passwordEncoder.encode(userPassword))
                .userPasswordConfirm(passwordEncoder.encode(userPasswordConfirm))
                .authority(Authority.ROLE_USER)
                .build();
    }
}
