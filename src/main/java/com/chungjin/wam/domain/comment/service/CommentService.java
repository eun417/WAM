package com.chungjin.wam.domain.comment.service;

import com.chungjin.wam.domain.member.entity.Member;
import com.chungjin.wam.domain.member.repository.MemberRepository;
import com.chungjin.wam.domain.comment.dto.request.CommentRequestDto;
import com.chungjin.wam.domain.comment.dto.response.CommentDto;
import com.chungjin.wam.domain.comment.entity.Comment;
import com.chungjin.wam.domain.support.entity.Support;
import com.chungjin.wam.domain.comment.repository.CommentRepository;
import com.chungjin.wam.domain.support.repository.SupportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final SupportRepository supportRepository;

    /**
     * 댓글 생성
     */
    public void createComment(String email, Long supportId, CommentRequestDto commentReq) {
        //email로 Member 객체 가져오기
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
        //supportId로 Support 객체 가져오기
        Support support = supportRepository.findById(supportId).orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));

        //Dto -> Entity
        Comment comment = Comment.builder()
                .content(commentReq.getContent())
                .support(support)
                .member(member)
                .build();

        //DB에 저장
        commentRepository.save(comment);
    }

    /**
     * 댓글 List 조회
     */
    public List<CommentDto> findAllComment(Long supportId) {
        //supportId에 해당하는 모든 댓글 가져오기
        List<Comment> comments = commentRepository.findAllBySupportId(supportId);

        /*
        comments -> stream
        Dto -> Entity
        map 메소드로 각 댓글을 CommentDto로 변환
        */
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
     */
    public void deleteComment(String email, Long supportId, Long commentId) {
        //supportId로 Support 객체 가져오기
        Support support = supportRepository.findById(supportId).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "존재하지 않는 후원 입니다."));
        //commentId로 Comment 객체 가져오기
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "존재하지 않는 댓글 입니다."));

        //로그인한 사용자가 작성자가 아닌 경우 에러 발생
        if(!email.equals(support.getMember().getEmail())) throw new ResponseStatusException(FORBIDDEN, "접근권한이 없습니다.");

        //DB에서 영구 삭제
        commentRepository.delete(comment);
    }

}
