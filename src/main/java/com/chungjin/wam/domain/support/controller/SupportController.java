package com.chungjin.wam.domain.support.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/support")
public class SupportController {

    /**
     * 후원 목록 조회
     */
    @GetMapping("/list")
    public String goSupportList() {
        return "donation/donationList";
    }

    /**
     * 후원 상세 조회
     */
    @GetMapping("/detail/{supportId}")
    public String goSupportDetail(@PathVariable("supportId") Long supportId, Model model) {
        model.addAttribute("supportId", supportId);
        return "donation/donationDetail";
    }

    /**
     * 후원 작성폼
     */
    @GetMapping({"/write", "/update/{supportId}"})
    public String goSupportWrite(@PathVariable("supportId") Optional<Long> supportId, Model model) {
        //supportId 생략 위해 Optional 사용
        if (supportId.isPresent()) {
            model.addAttribute("supportId", supportId);
        }
        return "donation/donationWrite";
    }

    /**
     * 검색
     */
    @GetMapping("/search")
    public String goSupportSearch(@RequestParam("search") String search) {
        return "donation/donationSearch";
    }

}
