package com.chungjin.wam.domain.support.controller;

import com.chungjin.wam.domain.support.dto.request.CommentRequestDto;
import com.chungjin.wam.domain.support.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 작성
     */
    @PostMapping("/support/{supportId}/comment")
    public ResponseEntity<String> createComment(@PathVariable(value = "supportId") Long supportId, @RequestBody CommentRequestDto commentReq) {
        commentService.createComment(supportId, commentReq);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    /**
     * 댓글 삭제
     */
    @DeleteMapping("/support/{supportId}/comment/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable(value = "commentId") Long commentId) {
        commentService.deleteComment(commentId);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

}
