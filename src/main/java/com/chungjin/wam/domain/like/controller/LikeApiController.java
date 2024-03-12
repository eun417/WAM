package com.chungjin.wam.domain.like.controller;

import com.chungjin.wam.domain.auth.dto.CustomUserDetails;
import com.chungjin.wam.domain.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class LikeApiController {

    private final LikeService likeService;

    /**
     * 좋아요 생성
     */
    @PostMapping("/support/{supportId}/like")
    public ResponseEntity<String> createLike(@AuthenticationPrincipal CustomUserDetails userDetails,
                                             @PathVariable(value = "supportId") Long supportId) {
        likeService.createLike(userDetails.getMember().getMemberId(), supportId);
        return ResponseEntity.ok("좋아요가 생성되었습니다.");
    }

    /**
     * 좋아요 삭제
     */
    @DeleteMapping("/support/{supportId}/like/{supportLikeId}")
    public ResponseEntity<String> deleteLike(@AuthenticationPrincipal CustomUserDetails userDetails,
                                             @PathVariable(value = "supportId") Long supportId,
                                             @PathVariable(value = "supportLikeId") Long supportLikeId) {
        likeService.deleteLike(userDetails.getMember().getMemberId(), supportId, supportLikeId);
        return ResponseEntity.ok("좋아요가 삭제되었습니다.");
    }

}
