package com.example.jwtproject.service;

import com.example.jwtproject.controller.request.LoginRequestDto;
import com.example.jwtproject.controller.request.MemberRequestDto;
import com.example.jwtproject.controller.request.TokenDto;
import com.example.jwtproject.controller.response.MemberResponseDto;
import com.example.jwtproject.controller.response.ResponseDto;
import com.example.jwtproject.domain.Member;
import com.example.jwtproject.jwt.TokenProvider;
import com.example.jwtproject.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, TokenProvider tokenProvider) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    // Todo :: 회원가입
    @Transactional
    public ResponseDto<?> createMember(MemberRequestDto memberRequestDto) {
        if (null != isPresentMember(memberRequestDto.getNickname())) {
            return ResponseDto.fail("DUPLICATED_NICKNAME", "중복된 닉네임 입니다.");
        }

        if (!memberRequestDto.getPassword().equals(memberRequestDto.getPasswordConfirm())) {
            return ResponseDto.fail("PASSWORDS_NOT_MATCHED", "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        Member member = Member.builder()
                .nickname(memberRequestDto.getNickname()) // 전달받은 MemberRequestDto 에서 Nickname 을 가져옴
                .email(memberRequestDto.getEmail())
                .password(passwordEncoder.encode(memberRequestDto.getPassword())) // passwordEncoder 는 SecurityConfig 에서 @Bean 으로 등록해주었음.
                .build();

        memberRepository.save(member);

        // ResponseDto<T> 의 T 타입은 MemberResponseDto
        return ResponseDto.success(
                MemberResponseDto.builder()
                        .id(member.getId())
                        .nickname(member.getNickname())
                        .email(member.getEmail())
                        .createAt(member.getCreateAt())
                        .modifiedAt(member.getModifiedAt())
                        .build()
        );
    }

    // Todo :: 로그인
    @Transactional                                          // 웹브라우저 header 에 응답을 돌려줄 빈 객체
    public ResponseDto<?> login(LoginRequestDto requestDto, HttpServletResponse response) {

        Member member = isPresentMember(requestDto.getNickname()); // DB 에서 닉네임에 맞는 Member 를 가져왔음

        if (null == member) { // isPresentMember 의 메소드에 반환값이 .orElse 에 의해 null 이 반환되면
            return ResponseDto.fail("MEMBER_NOT_FOUND", "사용자를 찾을 수 없습니다.");
        }

        if (!member.validatePassword(passwordEncoder, requestDto.getPassword())) {
            return ResponseDto.fail("INVALID_MEMBER", "사용자를 찾을 수 없습니다.");
        }

        // Todo :: 회원정보 일치시 TokenProvider 로 보내 Token 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(member);

        // 웹브라우저 헤더 값 반환
        tokenToHeaders(tokenDto, response);

        return ResponseDto.success(
                MemberResponseDto.builder()
                        .id(member.getId())
                        .nickname(member.getNickname())
                        .email(member.getEmail())
                        .createAt(member.getCreateAt())
                        .modifiedAt(member.getModifiedAt())
                        .build()
        );
    }

    // Todo :: 로그아웃
    // header 에 있는 정보를 받기위해 HttpServletRequest 를 받음
    @Transactional(readOnly = true)
    public ResponseDto<?> logout(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Member member = tokenProvider.getMemberFromAuthentication();

        if (null == member) {
            return ResponseDto.fail("MEMBER_NOT_FOUND", "사용자를 찾을 수 없습니다.");
        }

        return tokenProvider.deleteRefreshToken(member);
    }


    /* ============================================================================================================== */
    /* ============================================================================================================== */
    /* ============================================================================================================== */


    // Todo :: 입력한 nickname 값이 있는지 DB 조회
    @Transactional(readOnly = true)
    public Member isPresentMember(String nickname) {
        Optional<Member> optionalMember = memberRepository.findByNickname(nickname);
        return optionalMember.orElse(null); // Todo :: .orElse() 는 해당 값이 null 이거나 null 이 아닌 경우에도 실행된다.
                                                  // Todo :: .orElseGet() 는 반드시 null 일때만 실행된다. 더 차이가 있지만 간단히 적어보았다.
    }

    // Todo :: Headers 로 보낼 TokenDto , Header 로 반환할 빈 객체 response
    public void tokenToHeaders(TokenDto tokenDto, HttpServletResponse response) {
        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("Refresh-Token", tokenDto.getRefreshToken());
        response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());
    }


}
