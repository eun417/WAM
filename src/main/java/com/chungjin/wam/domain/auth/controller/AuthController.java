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
        return "join";
    }

    @GetMapping("/signup/agree")
    public String signupAgree() {
        return "joinAgree";
    }

    @GetMapping("/signup/email")
    public String signupEmail() {
        return "joinEmail";
    }

    @GetMapping("/signup/form")
    public String signupForm() {
        return "joinForm";
    }

    /**
     * 로그인
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * 이메일 찾기
     */
    @GetMapping("/find-account")
    public String findEmail() {
        return "loginFindEmail";
    }

    /**
     * 비밀번호 재설정
     */
    @GetMapping("/change-pw/{authCode}")
    public String changePw() {
        return "loginFindPassword";
    }

}
