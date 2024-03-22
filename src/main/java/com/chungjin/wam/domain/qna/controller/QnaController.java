package com.chungjin.wam.domain.qna.controller;

import com.chungjin.wam.domain.auth.dto.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/qna")
public class QnaController {

    /**
     * QnA 목록 조회
     */
    @GetMapping("/list")
    public String goQnaList() {
        return "qna/qnaList";
    }

    /**
     * QnA 상세 조회
     */
    @GetMapping("/detail/{qnaId}")
    public String goQnaDetail(@PathVariable("qnaId") Long qnaId, Model model) {
        model.addAttribute("qnaId", qnaId);
        return "qna/qnaDetail";
    }

    /**
     * QnA 작성폼
     */
    @GetMapping("/write")
    public String goQnaWrite() {
        return "qna/qnaWrite";
    }

    /**
     * 검색
     */
    @GetMapping("/search")
    public String goQnaSearch(@RequestParam("select") String select,
                              @RequestParam("search") String search) {
        return "qna/qnaSearch";
    }

}
