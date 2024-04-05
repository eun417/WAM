package com.chungjin.wam.domain.qna.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

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
    @GetMapping({"/write", "/update/{qnaId}"})
    public String goQnaWrite(@PathVariable("qnaId") Optional<Long> qnaId, Model model) {
        if (qnaId.isPresent()) {
            model.addAttribute("qnaId", qnaId);
        }
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
