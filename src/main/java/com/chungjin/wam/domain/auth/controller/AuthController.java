package com.chungjin.wam.domain.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {

    /**
     * 회원가입
     */
    @GetMapping("/signup/main")
    public String signupMain() {
        return "join/join";
    }

    @GetMapping("/signup/agree")
    public String signupAgree() {
        return "join/joinAgree";
    }

    @GetMapping("/signup/email")
    public String signupEmail() {
        return "join/joinEmail";
    }

    @GetMapping("/signup/form")
    public String signupForm() {
        return "join/joinForm";
    }

    /**
     * 로그인
     */
    @GetMapping("/login")
    public String login() {
        return "login/login";
    }

    /**
     * 이메일 찾기
     */
    @GetMapping("/find-account")
    public String findEmail() {
        return "login/loginFindEmail";
    }

    /**
     * 비밀번호 재설정
     */
    @GetMapping("/change-pw/{authCode}")
    public String changePw() {
        return "login/loginFindPassword";
    }

}
