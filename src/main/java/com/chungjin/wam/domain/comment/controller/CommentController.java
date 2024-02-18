package com.chungjin.wam.domain.comment.controller;

import com.chungjin.wam.domain.comment.dto.request.CommentRequestDto;
import com.chungjin.wam.domain.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 생성
     */
    @PostMapping("/support/{supportId}/comment")
    public ResponseEntity<String> createComment(@AuthenticationPrincipal User user,
                                                @PathVariable(value = "supportId") Long supportId,
                                                @RequestBody @Valid CommentRequestDto commentReq) {
        commentService.createComment(user.getUsername(), supportId, commentReq);
        return ResponseEntity.ok("success");
    }

    /**
     * 댓글 삭제
     */
    @DeleteMapping("/support/{supportId}/comment/{commentId}")
    public ResponseEntity<String> deleteComment(@AuthenticationPrincipal User user,
                                                @PathVariable(value = "supportId") Long supportId,
                                                @PathVariable(value = "commentId") Long commentId) {
        commentService.deleteComment(user.getUsername(), supportId, commentId);
        return ResponseEntity.ok("success");
    }

}
