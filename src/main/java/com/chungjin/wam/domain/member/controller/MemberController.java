package com.chungjin.wam.domain.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
public class MemberController {

    @GetMapping("/mypage")
    public String mypage() {
        return "mypage/myMember";
    }

    @GetMapping("/mypage/like")
    public String myLike() {
        return "mypage/myLike";
    }

    @GetMapping("/mypage/support")
    public String mySupport() {
        return "mypage/mySupport";
    }

    @GetMapping("/mypage/qna")
    public String myQna() {
        return "mypage/myQna";
    }

}
