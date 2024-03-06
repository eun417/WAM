package com.chungjin.wam.global.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    /**
     * Auth
     */

    //회원가입
    @GetMapping("/auth/signup/main")
    public String signupMain() {
        return "join";
    }

    @GetMapping("/auth/signup/agree")
    public String signupAgree() {
        return "joinAgree";
    }

    @GetMapping("/auth/signup/email")
    public String signupEmail() {
        return "joinEmail";
    }

    @GetMapping("/auth/signup/form")
    public String signupForm() {
        return "joinForm";
    }

    //로그인
    @GetMapping("/auth/login")
    public String login() {
        return "login";
    }

    //이메일 찾기
    @GetMapping("/auth/find-account")
    public String findEmail() {
        return "loginFindEmail";
    }

    //비밀번호 재설정
    @GetMapping("/auth/change-pw")
    public String changePw() {
        return "loginFindPassword";
    }

}
