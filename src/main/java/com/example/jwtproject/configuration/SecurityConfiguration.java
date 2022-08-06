package com.example.jwtproject.configuration;

import com.example.jwtproject.jwt.AccessDeniedHandlerException;
import com.example.jwtproject.jwt.AuthenticationEntryPointException;
import com.example.jwtproject.jwt.TokenProvider;
import com.example.jwtproject.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.ConditionalOnDefaultWebSecurity;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // Todo :: Bean 객체 등록 가능x
@EnableWebSecurity // Todo :: Security 활성화
@ConditionalOnDefaultWebSecurity
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class SecurityConfiguration {

    @Value("${jwt.secret}")
    String SECRET_KEY;

    private final TokenProvider tokenProvider;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationEntryPointException authenticationEntryPointException;
    private final AccessDeniedHandlerException accessDeniedHandlerException;

    public SecurityConfiguration(TokenProvider tokenProvider, UserDetailsServiceImpl userDetailsService, AuthenticationEntryPointException authenticationEntryPointException, AccessDeniedHandlerException accessDeniedHandlerException) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
        this.authenticationEntryPointException = authenticationEntryPointException;
        this.accessDeniedHandlerException = accessDeniedHandlerException;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // h2-console 사용에 대한 허용 (CSRF, FrameOptions 무시)
        return (web) -> web.ignoring()
                .antMatchers("/h2-console/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { // Todo :: WebSecurity , HttpSecurity 정보 확인

        http.cors();

        http.csrf() // 사이트 간 요청 위조 (Cross-site request forgery) 사용자 의지와 상관없이 공격자가 수정,삭제,등록 하는 것
                .disable()

                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPointException) // 우리가 만든 401 error 를 보여줌
                .accessDeniedHandler(accessDeniedHandlerException) // 우리가 만든 403 error 를 보여줌

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // spring security 는 기본 session 동작이기에 세션은 무상태로 만들어 사용하지 않음

                .and()
                .authorizeRequests()
                .antMatchers("/member/**").permitAll()
                .antMatchers("/all-post").permitAll()
//                .antMatchers("/api/post/**").permitAll()
//                .antMatchers("/api/comment/**").permitAll()
                .anyRequest().authenticated() // 나머지는 api 는 전부 인증해야 됌

                .and()
                .apply(new JwtSecurityConfiguration(SECRET_KEY, tokenProvider, userDetailsService));

        return http.build();
    }
}
