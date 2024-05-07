package com.chungjin.wam.domain.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    /**
     * Member 목록 조회
     */
    @GetMapping("/member/list")
    public String goMemberList() {
        return "admin/memberList";
    }

    /**
     * QnA 목록 조회
     */
    @GetMapping("/qna/list")
    public String goQnaList() {
        return "admin/qnaList";
    }

    /**
     * Support 목록 조회
     */
    @GetMapping("/support/list")
    public String goSupportList() {
        return "admin/supportList";
    }

    /**
     * 야생동물 지도 파일 목록 조회
     */
    @GetMapping("/animal-map")
    public String goAnimalMapList() {
        return "admin/animalMap";
    }

}
