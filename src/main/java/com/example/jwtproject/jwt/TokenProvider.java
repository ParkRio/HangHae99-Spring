package com.example.jwtproject.jwt;

import com.example.jwtproject.controller.request.TokenDto;
import com.example.jwtproject.controller.response.ResponseDto;
import com.example.jwtproject.domain.Member;
import com.example.jwtproject.domain.RefreshToken;
import com.example.jwtproject.domain.UserDetailsImpl;
import com.example.jwtproject.repository.RefreshTokenRepository;
import com.example.jwtproject.shared.Authority;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Slf4j // log 를 보기 위함
@Component // bean 으로 등록
public class TokenProvider {

    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_PREFIX = "Bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; // 30 분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7; // 7일

    private final Key key;

    private final RefreshTokenRepository refreshTokenRepository;

    // Todo :: 토큰 생성 생성자 (필요한 암호화 키를 생성하기 위함, 리프레시 토큰 레포지토리 주입)
    public TokenProvider(@Value("${jwt.secret}") String secretKey, RefreshTokenRepository refreshTokenRepository) {
        // Todo :: 필드의 key = secretKey, refreshTokenRepository = refreshTokenRepository
        this.refreshTokenRepository = refreshTokenRepository;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey); // Keys.hmacShaKeyFor(byte[] bytes)
        this.key = Keys.hmacShaKeyFor(keyBytes); // Todo :: hmacShaKeyFor 알고리즘으로 암호화
    }

    // Todo :: 토큰 생성
    public TokenDto generateTokenDto(Member member) {

        long now = (new Date().getTime());

        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        Date refreshTokenExpiresIn = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);

        // Todo :: 토큰의 반환 값은 String 이다.
        // Todo :: base64url_encode(Header) + '.' + base64url_encode(Claims) + '.' + base64url_encode(Signature)

        // accessToken 생성
        // 이 코드에서 헤더 부분은 MemberService -> tokenToHeaders 에 구현되어 Service 에서 보내줌.
        // 헤더는 .headers() 통해 이곳에서도 설정 가능.
        String accessToken = Jwts.builder()
                .setSubject(member.getNickname()) // 토큰 용도
                .claim(AUTHORITIES_KEY, Authority.ROLE_MEMBER.toString()) // payload 에 들어갈 것
                .setExpiration(accessTokenExpiresIn) // 토큰 만료 시간
                .signWith(key, SignatureAlgorithm.HS256) // 비밀 키, 서명 알고리즘 // Todo :: 서명 알고리즘으로 HS256을 선택한다면 비밀키는 별도의 클래스에서 관리한다.
                                                                              // Todo :: 비밀 키는 공개되면 안되기 때문에 깃허브에 올리기전 .gitignore 에 추가해두자.
                .compact(); // 위 설정대로 Token 을 생성한다.

        // refreshToken 생성
        String refreshToken = Jwts.builder()
                .setExpiration(refreshTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // refreshToken을 데이터베이스에 저장하기 위해 RefreshToken Entity에 넣음
        RefreshToken refreshTokenObject = RefreshToken.builder()
                .id(member.getId())
                .member(member)
                .value(refreshToken)
                .build();

        refreshTokenRepository.save(refreshTokenObject);

        return TokenDto.builder()
                .grantType(BEARER_PREFIX) // Todo :: 다양한 클라이언트 환경에 적합한 인증 및 권한의 위임 방법(grant_type)을 제공하고 그 결과로 클라이언트에게 access_token을 발급하는 것이다.
                                          // Todo :: 한 번 획득된 access_token은 만료 시점까지 모든 리소스 서버의 엔드포인트 요청 헤더에 Authorization: Bearer {ACCESS_TOKEN}로 첨부된다.
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn.getTime()) // type Long -> .getTime() return Long
                .refreshToken(refreshToken)
                .build();
    }

    // Todo :: 토큰 권한 입증
    public boolean validateToken(String token) {
        // Exception 해결을 위한 try catch ->
        try {

            Jwts
                    .parserBuilder()        // Jwts.parseBuilder 메서드를 이용해서 JwtParse Builder 인스턴스를 생성한다.
                    .setSigningKey(key)     // JWS 서명 검증을 위한 SecretKey 혹은 비대칭 PublicKey를 지정한다.
                    .build()                // 스레드에 안전한 JwtPaser 를 리턴하기 위해 JwtPaserBuilder 의 build() 메서드를 호출한다.
                    .parseClaimsJws(token); // 마지막으로 원본 JWS 를 생성하는 jws 를 가지고 parseClaimsJws(String)메서드를 호출한다.

            return true;

        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature, 유효하지 않은 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token, 지원되지 않는 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims is empty, 잘못된 JWT 토큰입니다.");
        }

        return false;
    }

    // Todo :: 시큐리티가 인증한 내용을 가지고 오자.
    public Member getMemberFromAuthentication() {
        // 시큐리티가 인증한 내용을 가지고 오자!
        Authentication authentication = SecurityContextHolder // SecurityContextHolder는 시큐리티가 인증한 내용들을 가지고 있으며, SecurityContext를 포함하고 있고 SecurityContext를 현재 스레드와 연결해 주는 역할을 합니다.
                .getContext()
                .getAuthentication();

        // 사용자가 로그인 중인지 확인하기 위함 ( 이를 위해 위에서 시큐리티 인증내용 조회함 )
        if (authentication == null || AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
            return null;
        }

        // Authentication 객체의 getPrincipal() 메서드를 실행하게 되면, UserDetails 를 구현한 사용자 객체를 Return 한다.
        // UserDetails 를 구현한 객체가 가지고 있는 정보들을 가져올 수 있다
        return ((UserDetailsImpl) authentication.getPrincipal()).getMember();

    }

    // Todo :: RefreshToken 삭제
    @Transactional
    public ResponseDto<?> deleteRefreshToken(Member member) {

        RefreshToken refreshToken = isPresentRefreshToken(member);

        if (null == refreshToken) {
            return ResponseDto.fail("TOKEN_NOT_FOUND", "존재하지 않는 Token 입니다.");
        }

        refreshTokenRepository.delete(refreshToken);

        return ResponseDto.success("success");
    }

    // Todo :: RefreshToken 을 데이터베이스에서 찾아옴.
    @Transactional
    public RefreshToken isPresentRefreshToken(Member member) {
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByMember(member);
        return optionalRefreshToken.orElse(null); // 값이 없으면 null 반환
    }



}
