package com.chungjin.wam.domain.auth.controller;

import com.chungjin.wam.domain.auth.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final RedisService redisService;

    /**
     * 회원가입
     */
    @GetMapping("/signup/main")
    public String goSignupMain() {
        return "join/join";
    }

    @GetMapping("/signup/agree")
    public String goSignupAgree() {
        return "join/joinAgree";
    }

    @GetMapping("/signup/form")
    public String goSignupForm() {
        return "join/joinForm";
    }

    /**
     * 로그인
     */
    @GetMapping("/login")
    public String goLogin() {
        return "login/login";
    }

    /**
     * 이메일 찾기
     */
    @GetMapping("/find-account")
    public String goFindEmail() {
        return "login/loginFindEmail";
    }

    /**
     * 비밀번호 재설정
     */
    @GetMapping("/change-pw")
    public String goChangePw() {
        return "login/loginChangePw";
    }

    @GetMapping("/change-pw/form")
    public String goChangePwForm(@RequestParam("authCode") String authCode) {
        return "login/loginChangePwForm";
    }

}
