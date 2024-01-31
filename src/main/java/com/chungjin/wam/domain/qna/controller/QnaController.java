package com.chungjin.wam.domain.qna.controller;

import com.chungjin.wam.domain.qna.dto.QnaDto;
import com.chungjin.wam.domain.qna.service.QnaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/qna")
public class QnaController {

    private final QnaService qnaService;

    /**
     * QnA 조회
     * */
    @GetMapping("/{qnaId}")
    public ResponseEntity<QnaDto> readQna(@PathVariable(value = "qnaId") Long qnaId) {
        return ResponseEntity.ok().body(qnaService.readQna(qnaId));
    }

}