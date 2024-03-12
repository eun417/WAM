package com.chungjin.wam.domain.comment.controller;

import com.chungjin.wam.domain.auth.dto.CustomUserDetails;
import com.chungjin.wam.domain.comment.dto.request.CommentRequestDto;
import com.chungjin.wam.domain.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentApiController {

    private final CommentService commentService;

    /**
     * 댓글 생성
     */
    @PostMapping("/support/{supportId}/comment")
    public ResponseEntity<String> createComment(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                @PathVariable(value = "supportId") Long supportId,
                                                @RequestBody @Valid CommentRequestDto commentReq) {
        commentService.createComment(userDetails.getMember().getMemberId(), supportId, commentReq);
        return ResponseEntity.ok("댓글을 작성되었습니다.");
    }

    /**
     * 댓글 삭제
     */
    @DeleteMapping("/support/{supportId}/comment/{commentId}")
    public ResponseEntity<String> deleteComment(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                @PathVariable(value = "supportId") Long supportId,
                                                @PathVariable(value = "commentId") Long commentId) {
        commentService.deleteComment(userDetails.getMember().getMemberId(), supportId, commentId);
        return ResponseEntity.ok("댓글이 삭제되었습니다.");
    }

}
