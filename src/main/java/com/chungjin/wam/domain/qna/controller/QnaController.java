package com.chungjin.wam.domain.qna.controller;

import com.chungjin.wam.domain.auth.service.CustomUserDetails;
import com.chungjin.wam.domain.qna.dto.request.QnaAnswerRequestDto;
import com.chungjin.wam.domain.qna.dto.request.QnaRequestDto;
import com.chungjin.wam.domain.qna.dto.request.UpdateQnaRequestDto;
import com.chungjin.wam.domain.qna.dto.response.QnaDetailDto;
import com.chungjin.wam.domain.qna.dto.response.QnaResponseDto;
import com.chungjin.wam.domain.qna.service.QnaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<String> createQna(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @RequestBody @Valid QnaRequestDto qnaReq) {
        qnaService.createQna(userDetails.getMember().getMemberId(), qnaReq);
        return ResponseEntity.ok("success");
    }

    /**
     * QnA 조회
     */
    @GetMapping("/{qnaId}")
    public ResponseEntity<QnaDetailDto> readQna(@PathVariable(value = "qnaId") Long qnaId) {
        return ResponseEntity.ok().body(qnaService.readQna(qnaId));
    }

    /**
     * QnA List 조회 (Pagination)
     */
    @GetMapping("/page={page}")
    public ResponseEntity<List<QnaResponseDto>> readAllQna(@PathVariable(value = "page") int page) {
        return ResponseEntity.ok().body(qnaService.readAllQna(page));
    }

    /**
     * QnA 수정
     */
    @PutMapping("/{qnaId}")
    public ResponseEntity<String> updateQna(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @PathVariable(value = "qnaId") Long qnaId,
                                            @RequestBody @Valid  UpdateQnaRequestDto updateQnaReq) {
        qnaService.updateQna(userDetails.getMember().getMemberId(), qnaId, updateQnaReq);
        return ResponseEntity.ok("success");
    }

    /**
     * QnA 삭제
     */
    @DeleteMapping("/{qnaId}")
    public ResponseEntity<String> deleteQna(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @PathVariable(value = "qnaId") Long qnaId) {
        qnaService.deleteQna(userDetails.getMember().getMemberId(), qnaId);
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

    /**
     * 검색 - 제목+내용
     */
    @GetMapping("/search/page={page}")
    public ResponseEntity<List<QnaResponseDto>> searchQna(@RequestParam("keyword") String keyword,
                                                          @PathVariable(value = "page") int page) {
        return ResponseEntity.ok().body(qnaService.searchQna(keyword, page));
    }

    /**
     * 검색 - 작성자
     */
    @GetMapping("/search/writer/page={page}")
    public ResponseEntity<List<QnaResponseDto>> searchQnaWriter(@RequestParam("keyword") String keyword,
                                                                @PathVariable(value = "page") int page) {
        return ResponseEntity.ok().body(qnaService.searchQnaWriter(keyword, page));
    }

}