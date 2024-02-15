package com.chungjin.wam.domain.qna.controller;

import com.chungjin.wam.domain.qna.dto.QnaDto;
import com.chungjin.wam.domain.qna.dto.request.QnaAnswerRequestDto;
import com.chungjin.wam.domain.qna.dto.request.QnaRequestDto;
import com.chungjin.wam.domain.qna.dto.request.UpdateQnaRequestDto;
import com.chungjin.wam.domain.qna.service.QnaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/qna")
public class QnaController {

    private final QnaService qnaService;

    /**
     * QnA 생성
     */
    @PostMapping("/")
    public ResponseEntity<String> createQna(@AuthenticationPrincipal User user, @RequestBody @Valid QnaRequestDto qnaReq) {
        qnaService.createQna(user.getUsername(), qnaReq);
        return ResponseEntity.ok("success");
    }

    /**
     * QnA 조회
     */
    @GetMapping("/{qnaId}")
    public ResponseEntity<QnaDto> readQna(@PathVariable(value = "qnaId") Long qnaId) {
        return ResponseEntity.ok().body(qnaService.readQna(qnaId));
    }

    /**
     * QnA List 조회 (Pagination)
     */
    @GetMapping("/page={page}")
    public ResponseEntity<List<QnaDto>> readAllQna(@PathVariable(value = "page") int page) {
        return ResponseEntity.ok().body(qnaService.readAllQna(page));
    }

    /**
     * QnA 수정
     */
    @PutMapping("/{qnaId}")
    public ResponseEntity<String> updateQna(@AuthenticationPrincipal User user,
                                            @PathVariable(value = "qnaId") Long qnaId,
                                            @RequestBody @Valid  UpdateQnaRequestDto updateQnaReq) {
        qnaService.updateQna(user.getUsername(), qnaId, updateQnaReq);
        return ResponseEntity.ok("success");
    }

    /**
     * QnA 삭제
     */
    @DeleteMapping("/{qnaId}")
    public ResponseEntity<String> deleteQna(@AuthenticationPrincipal User user,
                                            @PathVariable(value = "qnaId") Long qnaId) {
        qnaService.deleteQna(user.getUsername(), qnaId);
        return ResponseEntity.ok("success");
    }

    /**
     * QnA 답변 등록 (관리자만 가능)
     */
    @PutMapping("/{qnaId}/answer")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> updateQnaAnswer(@PathVariable(value = "qnaId") Long qnaId,
                                                  @RequestBody @Valid QnaAnswerRequestDto qnaAnswerReq) {
        qnaService.updateQnaAnswer(qnaId, qnaAnswerReq);
        return ResponseEntity.ok("success");
    }

}