package com.sparta.week04.memberservice;

import com.sparta.week04.memberdto.MemberResponseDto;
import com.sparta.week04.repository.MemberRepository;
import com.sparta.week04.security.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    // private final PasswordEncoder passwordEncoder; // BCryptPasswordEncoder implements PasswordEncoder
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public MemberService(MemberRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.memberRepository = userRepository;
        // this.passwordEncoder = passwordEncoder; // BcryptPassword DI
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

//    @Transactional
//    public MemberResponseDto<?> registerUser(SignupRequestDto signupRequestDto) {
//        String userNickname = signupRequestDto.getUserNickname();
//        String userPassword = signupRequestDto.getUserPassword();
//        String userPasswordCheck = signupRequestDto.getUserPasswordConfirm();
//        Authority authority = Authority.ROLE_USER;
//        // 회원 ID 중복 확인
//        Optional<Member> findUser = memberRepository.findByUserNickname(userNickname);
//        if (findUser.isPresent()) {
//            return MemberResponseDto.fail("중복된 사용자 ID 가 존재합니다.","중복된 사용자 ID 가 존재합니다.");
//        }
//
//        if (!userPassword.equals(userPasswordCheck)) {
//            return MemberResponseDto.fail("입력하신 비밀번호가 서로 다릅니다.","입력하신 비밀번호가 서로 다릅니다.");
//        } else {
//            userPassword = bCryptPasswordEncoder.encode(signupRequestDto.getUserPassword());
//            Member user = new Member(userNickname, userPassword, userPasswordCheck, authority);
//            memberRepository.save(user);
//            return MemberResponseDto.success(user);
//        }
//    }
//
//
//    public MemberResponseDto<?> loginUser(LoginRequestDto loginRequestDto) {
//        String userNickname = loginRequestDto.getUserNickname();
//        String userPassword = loginRequestDto.getUserPassword();
//        Optional<Member> findUser = memberRepository.findByUserNickname(userNickname);
//
//        if (findUser.isEmpty()) {
//            return MemberResponseDto.fail("사용자를 찾을 수 없습니다.", "사용자를 찾을 수 없습니다.");
//        }
//
//        if (!bCryptPasswordEncoder.matches(userPassword, findUser.get().getUserPassword())) {
//            return MemberResponseDto.fail("비밀번호가 일치하지 않습니다.", "비밀번호가 일치하지 않습니다.");
//        }
//
//        return MemberResponseDto.success(findUser);
//    }

    /* *************************************************************************************************** */

//    내 정보를 가져올 때는 SecurityUtil.getCurrentMemberId() 를 사용합니다.
//    SecurityUtil.getCurrentMemberId()는 util 폴더에 정의되어 있습니다.
//    API 요청이 들어오면 필터에서 Access Token 을 복호화 해서 유저 정보를 꺼내 SecurityContext 라는 곳에 저장합니다.
//    SecurityContext 에 저장된 유저 정보는 전역으로 어디서든 꺼낼 수 있습니다.
//    SecurityUtil 클래스에서는 유저 정보에서 Member ID 만 반환하는 메소드가 정의되어 있습니다.

    @Transactional(readOnly = true)
    public MemberResponseDto<?> getMemberInfo(String userNickname) {
        return memberRepository.findByName(userNickname)
                .map(MemberResponseDto::of)
                .orElseThrow(() -> new RuntimeException("유저 정보가 없습니다."));
    }

    // Todo :: .map(MemberResponseDto::of) -> of는 MemberResponseDto 메소드, SecurityUtil.getCurrentMemberId() 은 package 안 util 폴더 SecurityUtil 메소드
    @Transactional(readOnly = true)
    public MemberResponseDto<?> getMyInfo() {
        return memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .map(MemberResponseDto::of)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다."));
    }
}
