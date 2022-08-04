package com.sparta.week04.security;

import com.sparta.week04.security.jwt.JwtAccessDeniedHandler;
import com.sparta.week04.security.jwt.JwtAuthenticationEntryPoint;
import com.sparta.week04.security.jwt.JwtSecurityConfig;
import com.sparta.week04.security.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 스프링 Security 지원을 가능하게 함
public class WebSecurityConfig {

    private final TokenProvider provider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    public WebSecurityConfig(TokenProvider provider, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtAccessDeniedHandler jwtAccessDeniedHandler) {
        this.provider = provider;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // h2-console 사용에 대한 허용 (CSRF, FrameOptions 무시)
        return (web) -> web.ignoring()
                .antMatchers("/h2-console/**");
    }

    @Bean // 비밀번호 암호화 // 스프링 시큐리티에서 지원
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf().disable() // CSRF 설정 disable 사용안함

                // exception handling 할 때 우리가 만든 클래스를 추가
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                // h2-console 을 위한 설정 추가
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                // 시큐리티는 기본적으로 세션을 사용
                // jwt 에서는 세션을 사용하지 않기에 세션 설정을 stateless 로 설정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // 로그인, 회원가입 API 는 토큰이 없는 상태에서 요청이 들어오기 때문에 permitAll 설정
                .and()
                .authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/api/**").hasRole("USER")
                .anyRequest().authenticated() // 나머지 API 는 전부 인증 필요

                .and()
                .apply(new JwtSecurityConfig(provider));

//        // // security에서 기본으로 생성하는 login페이지 사용 안 함
//        http
//                .httpBasic()
//                .disable();
//
//        //회원 관리 처리 API (POST /user/**) 에 대해 CSRF 무시 -> POST 요청을 위함.
//        http
//                .csrf().disable();
//                // .ignoringAntMatchers("/api/member/**");
//
//        http.authorizeRequests((authz) -> authz // authorizeRequests 요청에 의한 보안검사 시작.
//                // 인증 전에 해야 할 것은 여기위에 설정해주어야 함.
//                // image 폴더를 login 없이 허용
//                .antMatchers("/images/**").permitAll()
//                // css 폴더를 login 없이 허용
//                .antMatchers("/css/**").permitAll()
//                .antMatchers("/h2-console/**").permitAll()
//                .antMatchers("/api/member/signup").permitAll()
//                .antMatchers("/api/member/login").permitAll()
//                // 위 까지는 인증검사 없이도 허가를 내주었기 때문에 api가 동작한다.
//                .anyRequest().authenticated() // 나머지 어떤 요청에도 인증검사를 해야한다.
//                );
////                    // 로그인 기능 허용
////                .formLogin()
////                .loginPage("/user/login")
////                .defaultSuccessUrl("/")
////                .failureUrl("/user/login?error")
////                .permitAll()
////                .and()
////                    // 로그아웃 기능 허용
////                .logout()
////                .permitAll();

        return http.build();
    }
}