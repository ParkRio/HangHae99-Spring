package com.sparta.week04.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 스프링 Security 지원을 가능하게 함 , spring security 5.7 버전이상부터 더 이상 WebSecurityConfigurerAdapter 상속을 지원하지 않음
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                // 어떤 요청이든 '인증'
                .anyRequest().authenticated()
                .and()
                // 로그인 기능 허용
                .formLogin()
                .defaultSuccessUrl("/") // 로그인이 성공하게 되면 어디로 이동할까요?
                .permitAll() // (로그인 기능) 전체 허용
                .and()
                // 로그아웃 기능 허용
                .logout()
                .permitAll(); // (로그아웃 기능) 전체 허용

        return http.build();
    }
}
