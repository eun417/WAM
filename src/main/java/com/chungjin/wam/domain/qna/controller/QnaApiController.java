package com.chungjin.wam.domain.qna.controller;

import com.chungjin.wam.domain.auth.dto.CustomUserDetails;
import com.chungjin.wam.domain.qna.dto.request.QnaAnswerRequestDto;
import com.chungjin.wam.domain.qna.dto.request.QnaRequestDto;
import com.chungjin.wam.domain.qna.dto.request.UpdateQnaRequestDto;
import com.chungjin.wam.domain.qna.dto.response.QnaDetailDto;
import com.chungjin.wam.domain.qna.service.QnaService;
import com.chungjin.wam.global.common.PageResponse;
import com.chungjin.wam.global.s3.S3Service;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.chungjin.wam.global.util.Constants.S3_QNA;

@RestController
@RequiredArgsConstructor
@RequestMapping("/qna")
public class QnaApiController {

    private final QnaService qnaService;
    private final S3Service s3Service;

    /**
     * QnA 생성
     */
    @PostMapping("/")
    public ResponseEntity<String> createQna(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @RequestBody @Valid QnaRequestDto qnaReq) {
        qnaService.createQna(userDetails.getMember().getMemberId(), qnaReq);
        return ResponseEntity.ok("QnA 게시물이 작성되었습니다.");
    }

    /**
     * QnA 조회
     */
    @GetMapping("/{qnaId}")
    public ResponseEntity<QnaDetailDto> readQna(@PathVariable("qnaId") Long qnaId) {
        return ResponseEntity.ok().body(qnaService.readQna(qnaId));
    }

    /**
     * QnA List 조회 (Pagination)
     */
    @GetMapping
    public ResponseEntity<PageResponse> readAllQna(@RequestParam("pageNo") int pageNo) {
        return ResponseEntity.ok().body(qnaService.readAllQna(pageNo));
    }

    /**
     * QnA 수정
     */
    @PutMapping("/{qnaId}")
    public ResponseEntity<String> updateQna(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @PathVariable("qnaId") Long qnaId,
                                            @RequestBody @Valid  UpdateQnaRequestDto updateQnaReq) {
        qnaService.updateQna(userDetails.getMember().getMemberId(), qnaId, updateQnaReq);
        return ResponseEntity.ok("QnA 게시물이 수정되었습니다.");
    }

    /**
     * QnA 삭제
     */
    @DeleteMapping("/{qnaId}")
    public ResponseEntity<String> deleteQna(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @PathVariable("qnaId") Long qnaId) {
        qnaService.deleteQna(userDetails.getMember().getMemberId(), qnaId);
        return ResponseEntity.ok("QnA 게시물이 삭제되었습니다.");
    }

    /**
     * QnA 답변 등록 (관리자만 가능)
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{qnaId}/answer")
    public ResponseEntity<String> updateQnaAnswer(@PathVariable("qnaId") Long qnaId,
                                                  @RequestBody @Valid QnaAnswerRequestDto qnaAnswerReq) {
        qnaService.updateQnaAnswer(qnaId, qnaAnswerReq);
        return ResponseEntity.ok("QnA 답변이 등록되었습니다.");
    }

    /**
     * 검색 - 제목, 내용, 작성자
     */
    @GetMapping("/search-keyword")
    public ResponseEntity<PageResponse> searchQna(@RequestParam("search") String keyword,
                                                  @RequestParam("pageNo") int pageNo) {
        return ResponseEntity.ok().body(qnaService.searchQna(keyword, pageNo));
    }

}