package com.sparta.week04.security.jwt;

// Todo :: JWT 관련 --> TokenProvider : 유저 정보로 JWT 토큰을 만들거나 토큰을 바탕으로 유저 정보를 가져온다.

import com.sparta.week04.memberdto.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider {

    // 우리가 궁금해하던 bearer 는 위 형식에서 type 에 해당합니다. 토큰에는 많은 종류가 있고 서버는 다양한 종류의 토큰을 처리하기 위해
    // 전송받은 type 에 따라 토큰을 다르게 처리합니다.
    // 인증 타입 찾아보기

    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 5 * 1000 * 60; // 5분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7; // 7일

    private final Key key;

    // Todo :: key 생성
    // @Value -> application.yml 의 property 에서 설정한 property 값을 가져옴
    public TokenProvider(@Value("${jwt.secret}") String secretKey) {
        // 로직 안에서는 byte 단위의 secretKey 를 만들어 주어야 한다.

        // 알고리즘 선택
        byte[] KeyBytes = Decoders.BASE64.decode(secretKey);

        // 키 생성
        this.key = Keys.hmacShaKeyFor(KeyBytes);
    }

    // Todo :: Token 생성
    public TokenDto generateTokenDto(Authentication authentication) {
        // 권한들 가져오기
        // .stream() 컬렉션에 저장되어 있는 엘리먼트들을 하나씩 순회하면서 처리할 수 있는 코드 패턴이다.
        // .stream().map() 각각의 item을 변경하여 새로운 컨텐츠를 생성하는 기능
        // .Collectors Class의 join() 메서드는 문자 또는 문자열 배열의 다양한 요소를 단일 문자열 개체로 결합하는데 사용됩니다. stream() 을 사용하여 가능
        // java.util.stream.Collectors.joining() 은 매개변수를 사용하지 않는 가장 간단한 결합 방법입니다.
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime(); // expire 시간을 지정하기 위해 현재 시간을 가져온다.

        // Todo :: Access Token 생성
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME); // 토큰 만료시간 설정
        // 1.JwtBuilder 객체를 생성하고 Jwts.builder() 메서드를 이용한다.
        // 2.header 파라미터와 claims 를 추가하기 위해 JwtBuilder 메서드 호출
        // 3.JWT 를 서명하기 위해 SecretKey 나 PrivateKey 를 지정한다.
        // 4.마지막으로 압축하고 서명하기 위해 compact()를 호출하고, jws 를 생성한다.
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName()) // payload "sub" : "name" // Authentication 객체에서 getName() 으로 사용자 정보를 찾아옴
                .claim(AUTHORITIES_KEY, "ROLE_USER")  // payload "auth" : "ROLE_USER" // 현재 강제로 ROLE_USER를 넣음
                .setExpiration(accessTokenExpiresIn)  // payload "exp" : 123214124 (예시)
                .signWith(key, SignatureAlgorithm.HS512) // header "alg" : "HS512"
                .compact();


        // Todo :: Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();


        return TokenDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
                .refreshToken(refreshToken)
                .build();
    }

    // Todo :: 여기가 JWT 토큰이 나오는 곳인가?? 나도 몰라..
    // Todo :: Token 을 파라미터로 받아서 담겨있는 정보를 이용해 Authentication 객체를 리턴하는 메소드
    public Authentication getAuthentication(String accessToken) {
        // Token 복호화
        // Claim 정보에는 토큰에 부가적으로 싫어 보낼 정보를 담을 수 있다.
        // parseClaims 는 하단에 따로 메소드화 하여 구현시켜 놓았다.
        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // Claims 에서 권한 정보 가져오기
        // Collection<? extends GrantedAuthority> --> 계정이 갖고 있는 권한 목록을 리턴한다.
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 을 리턴
        // UserDetail 이란 Spring Security에서 사용자의 정보를 담는 인터페이스이다.
        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }


    // Todo :: 토큰의 유효성 검증을 수행하는 validateToken 메소드 추가
    public boolean validationToken(String token) {
        try {
            // 위에서 암호환한 Jwts를 복호화 해줌
            // 위에서 signWith key 를 활용하여 암호화 했으므로 복호활 할 setSigningKey 에도 동일한 key 값을 넣어줌
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalStateException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
