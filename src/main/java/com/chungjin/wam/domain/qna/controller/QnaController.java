package com.chungjin.wam.domain.qna.controller;

import com.chungjin.wam.domain.qna.dto.QnaAnswerRequestDto;
import com.chungjin.wam.domain.qna.dto.QnaDto;
import com.chungjin.wam.domain.qna.service.QnaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/qna")
public class QnaController {

    private final QnaService qnaService;

    /**
     * QnA 생성
     * */
    @PostMapping("/")
    public ResponseEntity<String> createQna(@RequestBody QnaDto qnaDto) {
        qnaService.createQna(qnaDto);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    /**
     * QnA 조회
     * */
    @GetMapping("/{qnaId}")
    public ResponseEntity<QnaDto> readQna(@PathVariable(value = "qnaId") Long qnaId) {
        return ResponseEntity.ok().body(qnaService.readQna(qnaId));
    }

    /**
     * QnA List 조회 (Pagination)
     * */
    @GetMapping("/page={page}")
    public ResponseEntity<List<QnaDto>> readAllQna(@PathVariable(value = "page") int page) {
        return ResponseEntity.ok().body(qnaService.readAllQna(page));
    }

    /**
     * QnA 수정
     * */
    @PutMapping("/{qnaId}")
    public ResponseEntity<String> updateQna(@PathVariable(value = "qnaId") Long qnaId,
                                            @RequestBody QnaDto updateQnaDto) {
        qnaService.updateQna(qnaId, updateQnaDto);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    /**
     * QnA 삭제
     * */
    @DeleteMapping("/{qnaId}")
    public ResponseEntity<String> deleteQna(@PathVariable(value = "qnaId") Long qnaId) {
        qnaService.deleteQna(qnaId);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    /**
     * QnA 답변 등록
     * */
    @PostMapping("/{qnaId}/answer")
    public ResponseEntity<String> updateQnaAnswer(@PathVariable(value = "qnaId") Long qnaId,
                                               @RequestBody QnaAnswerRequestDto qnaAnswerReq) {
        qnaService.updateQnaAnswer(qnaId, qnaAnswerReq);
        return ResponseEntity.ok("success");
    }

}