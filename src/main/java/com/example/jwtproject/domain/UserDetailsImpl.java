package com.example.jwtproject.domain;

import com.example.jwtproject.shared.Authority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Data // @Getter, @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails { // Todo :: UserDetails 는 spring security 에서 사용자 정보를 담는 인터페이스

    private Member member;

    // Todo :: getAuthorities() 계정의 권한 목록을 return
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(Authority.ROLE_MEMBER.toString());
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(authority);
        return authorities;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getNickname();
    }

    // Todo :: 계정의 만료 여부
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    // Todo :: 계정의 잠김 여부
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    // Todo :: 비밀번호 만료 여부
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    // Todo :: 계정의 활성화 여부
    @Override
    public boolean isEnabled() {
        return false;
    }
}
