package com.chungjin.wam.global.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    /**
     * Auth
     */
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

}
