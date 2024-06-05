package com.chungjin.wam.domain.support.controller;

import com.chungjin.wam.domain.auth.dto.CustomUserDetails;
import com.chungjin.wam.domain.support.dto.request.SupportRequestDto;
import com.chungjin.wam.domain.support.dto.request.UpdateSupportRequestDto;
import com.chungjin.wam.domain.support.dto.response.SupportDetailDto;
import com.chungjin.wam.domain.support.dto.response.SupportResponseDto;
import com.chungjin.wam.domain.support.entity.AnimalSubjects;
import com.chungjin.wam.domain.support.service.SupportService;
import com.chungjin.wam.global.common.PageResponse;
import com.chungjin.wam.global.s3.S3Service;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/support")
public class SupportApiController {

    private final SupportService supportService;
    private final S3Service s3Service;

    /**
     * 후원 생성
     */
    @PostMapping("/")
    public ResponseEntity<String> createSupport(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                @RequestPart("supportReq") @Valid SupportRequestDto supportReq,
                                                @RequestPart("firstImg") MultipartFile firstImg) {
        supportService.createSupport(userDetails.getMember().getMemberId(), supportReq, firstImg);
        return ResponseEntity.ok("후원이 생성되었습니다.");
    }

    /**
     * 후원 조회
     */
    @GetMapping("/{supportId}")
    public ResponseEntity<SupportDetailDto> readSupport(@PathVariable(value = "supportId") Long supportId) {
        return ResponseEntity.ok().body(supportService.readSupport(supportId));
    }

    /**
     * 후원 List 조회 (Pagination)
     */
    @GetMapping
    public ResponseEntity<PageResponse> readAllSupport(@RequestParam("pageNo") int pageNo) {
        return ResponseEntity.ok().body(supportService.readAllSupport(pageNo));
    }

    /**
     * 종료 임박 후원 List 조회
     */
    @GetMapping("/ending-soon")
    public ResponseEntity<List<SupportResponseDto>> readEndingSoonSupport() {
        return ResponseEntity.ok().body(supportService.readEndingSoonSupport());
    }

    /**
     * 후원 수정
     */
    @PutMapping("/{supportId}")
    public ResponseEntity<String> updateSupport(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                @PathVariable(value = "supportId") Long supportId,
                                                @RequestPart("updateSupportReq") @Valid UpdateSupportRequestDto updateSupportReq,
                                                @RequestPart(name = "newFirstImg", required = false) MultipartFile newFirstImg) {
        supportService.updateSupport(userDetails.getMember().getMemberId(), supportId, updateSupportReq, newFirstImg);
        return ResponseEntity.ok("후원이 수정되었습니다.");
    }

    /**
     * 후원 삭제
     */
    @DeleteMapping("/{supportId}")
    public ResponseEntity<String> deleteSupport(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                @PathVariable(value = "supportId") Long supportId) {
        supportService.deleteSupport(userDetails.getMember().getMemberId(), supportId);
        return ResponseEntity.ok("후원이 삭제되었습니다.");
    }

    /**
     * 검색 - 제목, 내용, 작성자
     */
    @GetMapping("/search-keyword")
    public ResponseEntity<PageResponse> searchSupport(@RequestParam("select") String select,
                                                      @RequestParam("keyword") String keyword,
                                                      @RequestParam("pageNo") int pageNo) {
        return ResponseEntity.ok().body(supportService.searchSupport(select, keyword, pageNo));
    }

    /**
     * 검색 - 태그(동물 분류)
     */
    @GetMapping("/search-tag")
    public ResponseEntity<PageResponse> searchSupportTag(@RequestParam("keyword") AnimalSubjects tagName,
                                                         @RequestParam("pageNo") int pageNo) {
        return ResponseEntity.ok().body(supportService.searchSupportTag(tagName, pageNo));
    }

}
