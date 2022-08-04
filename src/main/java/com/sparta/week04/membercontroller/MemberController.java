package com.sparta.week04.membercontroller;

import com.sparta.week04.memberdto.MemberResponseDto;
import com.sparta.week04.memberservice.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService userService) {
        this.memberService = userService;
    }

//    // 회원 로그인 페이지
//    @GetMapping("/user/login")
//    public String login() {
//        return "login";
//    }
//
//    // 회원 가입 페이지
//    @GetMapping("/user/signup")
//    public String signup() {
//        return "signup";
//    }
//
//    // 회원 가입 요청 처리
//    @ResponseBody
//    @PostMapping("/api/member/signup")
//    public MemberResponseDto<?> registerUser(@RequestBody SignupRequestDto requestDto) {
//        return memberService.registerUser(requestDto);
//    }
//
//    @ResponseBody
//    @PostMapping("/api/member/login")
//    public MemberResponseDto<?> loginUser(@RequestBody LoginRequestDto requestDto) {
//        return memberService.loginUser(requestDto);
//    }

    /* ******************************************************************************** */

    /**
     * ResponseEntity 란? spring framework 에서 제공하는 클래스 중 HttpEntity 라는 클래스가 존재한다. 이것은 HTTP 요청(Request) 또는
     * 응답(Response) 에 해당하는 HttpHeader 와 HttpBody 를 포함하는 클래스이다.
     */

    @ResponseBody
    @GetMapping("api/member/me")
    public ResponseEntity<MemberResponseDto<?>> getMyMemberInfo() {
        return ResponseEntity.ok(memberService.getMyInfo());
    }

    @ResponseBody
    @GetMapping("api/member/{userNickname}")
    public ResponseEntity<MemberResponseDto<?>> getMemberInfo(@PathVariable String userNickname) {
        return ResponseEntity.ok(memberService.getMemberInfo(userNickname));
    }
}
