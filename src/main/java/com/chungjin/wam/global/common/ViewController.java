package com.chungjin.wam.global.common;

import com.chungjin.wam.domain.auth.dto.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class ViewController {

    @GetMapping("/support/test")
    public String testPage() {
        return "testPage";
    }

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

    /**
     * Admin
     */

    //모든 Member 조회
    @GetMapping("/admin/member")
    public String readAllMember() {
        return "admin/memberList";
    }

    //모든 QnA 조회
    @GetMapping("/admin/qna")
    public String readAllQna() {
        return "admin/qnaList";
    }

    //모든 Support 조회
    @GetMapping("/admin/support")
    public String readAllSupport() {
        return "admin/supportList";
    }

    //모든 야생동물 지도 파일 조회
    @GetMapping("/admin/animal-map")
    public String readAllAnimalMap() {
        return "admin/animalMap";
    }

}
