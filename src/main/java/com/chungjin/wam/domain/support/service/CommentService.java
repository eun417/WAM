package com.chungjin.wam.domain.support.service;

import com.chungjin.wam.domain.member.entity.Member;
import com.chungjin.wam.domain.member.repository.MemberRepository;
import com.chungjin.wam.domain.support.dto.request.CommentRequestDto;
import com.chungjin.wam.domain.support.dto.response.CommentDto;
import com.chungjin.wam.domain.support.entity.Comment;
import com.chungjin.wam.domain.support.entity.Support;
import com.chungjin.wam.domain.support.repository.CommentRepository;
import com.chungjin.wam.domain.support.repository.SupportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final SupportRepository supportRepository;

    /**
     * 댓글 작성
     */
    public void createComment(Long supportId, CommentRequestDto commentReq) {
        Member member = memberRepository.findById(commentReq.getMemberId()).orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
        Support support = supportRepository.findById(supportId).orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));

        Comment comment = Comment.builder()
                .content(commentReq.getContent())
                .createDate(commentReq.getCreateDate())
                .support(support)
                .member(member)
                .build();

        commentRepository.save(comment);
    }

    /**
     * 댓글 List 조회
     * */
    public List<CommentDto> findAllComment(Long supportId) {
        List<Comment> comments = commentRepository.findAllBySupportId(supportId);
        return comments.stream()
                .map(comment -> CommentDto.builder()
                        .commentId(comment.getCommentId())
                        .email(comment.getMember().getEmail())
                        .content(comment.getContent())
                        .createDate(comment.getCreateDate())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 댓글 삭제
     * */
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

}
