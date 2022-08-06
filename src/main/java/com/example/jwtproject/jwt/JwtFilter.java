package com.example.jwtproject.jwt;

import com.example.jwtproject.controller.response.ResponseDto;
import com.example.jwtproject.service.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class JwtFilter extends OncePerRequestFilter {

    public static String AUTHORIZATION_HEADER = "Authorization";
    public static String BEARER_PREFIX = "Bearer";
    public static String AUTHORITIES_KEY = "auth";

    private final String SECRET_KEY;
    private final TokenProvider tokenProvider;
    private final UserDetailsServiceImpl userDetailsService; // user 정보

    public JwtFilter(String secret_key, TokenProvider tokenProvider, UserDetailsServiceImpl userDetailsService) {
        SECRET_KEY = secret_key;
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

    // Todo :: doFilter 메소드의 역할 : jwt 토큰의 인증정보를 현재 실행중인 SecurityContext 에 저장하는 역할 수행
    // Todo :: request 에서 토큰을 받아서 이 토큰을 validateToken 메소드로 유효성 검사를 하고
    // Todo :: 토큰이 정상적이면 토큰에서 authentication 객체를 받아와서 SecurityContext 에 set 해줌
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        String jwt = resolveToken(request);

        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            Claims claims;
            try {
                claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody();
            } catch (ExpiredJwtException e) {
                claims = e.getClaims();
            }

            if (claims.getExpiration().toInstant().toEpochMilli() < Instant.now().toEpochMilli()) {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter()
                        .println(
                                new ObjectMapper().writeValueAsString(
                                        ResponseDto.fail("BAD_REQUEST", "Token이 유효하지 않습니다.")
                                )
                        );
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }

            String subject = claims.getSubject();

            Collection<? extends GrantedAuthority> authorities =
                    Arrays.stream(
                            claims.get(AUTHORITIES_KEY)
                                    .toString()
                                    .split(","))
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

            UserDetails principal = userDetailsService.loadUserByUsername(subject);

            Authentication authentication = new UsernamePasswordAuthenticationToken(principal, jwt, authorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    /* ==================================================================================================== */
    /* ==================================================================================================== */
    /* ==================================================================================================== */

    // Todo :: 토큰 해결 ??
    // Todo :: resolveToken 메소드에서는 Request Header 에서 토큰 정보를 꺼내오기 위한 resolveToken 메소드 추가
    private String resolveToken(HttpServletRequest request) {

        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }

        return null;
    }
}
