package com.example.jwtproject.configuration;

import com.example.jwtproject.jwt.JwtFilter;
import com.example.jwtproject.jwt.TokenProvider;
import com.example.jwtproject.service.UserDetailsServiceImpl;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtSecurityConfiguration extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final String SECRET_KEY;
    private final TokenProvider tokenProvider;
    private final UserDetailsServiceImpl userDetailsService; // User 정보

    public JwtSecurityConfiguration(String secret_key, TokenProvider tokenProvider, UserDetailsServiceImpl userDetailsService) {
        SECRET_KEY = secret_key;
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }


    @Override
    public void configure(HttpSecurity httpSecurity) {

        JwtFilter customJwtFilter = new JwtFilter(SECRET_KEY, tokenProvider, userDetailsService);

        httpSecurity.addFilterBefore(customJwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
