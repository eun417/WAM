package com.chungjin.wam.domain.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member/")
public class MemberController {

    @GetMapping("/profile")
    public String mypage() {
        return "mypage/myMember";
    }

    @GetMapping("/like")
    public String myLike() {
        return "mypage/myLike";
    }

    @GetMapping("/support")
    public String mySupport() {
        return "mypage/mySupport";
    }

    @GetMapping("/qna")
    public String myQna() {
        return "mypage/myQna";
    }

    @GetMapping("/leave")
    public String leaveMember() {
        return "mypage/leaveMember";
    }

}
