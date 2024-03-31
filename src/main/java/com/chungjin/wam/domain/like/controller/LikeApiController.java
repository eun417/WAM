package com.chungjin.wam.domain.like.controller;

import com.chungjin.wam.domain.auth.dto.CustomUserDetails;
import com.chungjin.wam.domain.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/support")
public class LikeApiController {

    private final LikeService likeService;

    /**
     * 좋아요 생성
     */
    @PostMapping("/{supportId}/like")
    public ResponseEntity<String> createLike(@AuthenticationPrincipal CustomUserDetails userDetails,
                                             @PathVariable(value = "supportId") Long supportId) {
        likeService.createLike(userDetails.getMember().getMemberId(), supportId);
        return ResponseEntity.ok("좋아요가 생성되었습니다.");
    }

    /**
     * 좋아요 상태 조회
     */
    @GetMapping("/{supportId}/likeStatus")
    public ResponseEntity<Boolean> readLikeStatus(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                  @PathVariable(value = "supportId") Long supportId) {
        return ResponseEntity.ok(likeService.readLikeStatus(userDetails.getMember().getMemberId(), supportId));
    }

    /**
     * 좋아요 삭제
     */
    @DeleteMapping("/{supportId}/like")
    public ResponseEntity<String> deleteLike(@AuthenticationPrincipal CustomUserDetails userDetails,
                                             @PathVariable(value = "supportId") Long supportId) {
        likeService.deleteLike(userDetails.getMember().getMemberId(), supportId);
        return ResponseEntity.ok("좋아요가 삭제되었습니다.");
    }

}
