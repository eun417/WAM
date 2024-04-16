package com.chungjin.wam.domain.comment.service;

import com.chungjin.wam.domain.member.entity.Member;
import com.chungjin.wam.domain.member.repository.MemberRepository;
import com.chungjin.wam.domain.comment.dto.request.CommentRequestDto;
import com.chungjin.wam.domain.comment.dto.response.CommentDto;
import com.chungjin.wam.domain.comment.entity.Comment;
import com.chungjin.wam.domain.support.entity.Support;
import com.chungjin.wam.domain.comment.repository.CommentRepository;
import com.chungjin.wam.domain.support.repository.SupportRepository;
import com.chungjin.wam.global.exception.CustomException;
import com.chungjin.wam.global.exception.error.ErrorCodeType;
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
    public void createComment(Long memberId, Long supportId, CommentRequestDto commentReq) {
        //email로 Member 객체 가져오기
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCodeType.MEMBER_NOT_FOUND));
        //supportId로 Support 객체 가져오기
        Support support = getSupport(supportId);

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
                        .memberId(comment.getMember().getMemberId())
                        .nickname(comment.getMember().getNickname())
                        .content(comment.getContent())
                        .createDate(comment.getCreateDate())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 댓글 삭제
     */
    public void deleteComment(Long memberId, Long commentId) {
        //commentId로 Comment 객체 가져오기
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CustomException(ErrorCodeType.COMMENT_NOT_FOUND));

        //로그인한 사용자가 작성자가 아닌 경우 에러 발생
        if(!memberId.equals(comment.getMember().getMemberId())) throw new CustomException(ErrorCodeType.FORBIDDEN);

        //DB에서 영구 삭제
        commentRepository.delete(comment);
    }

    //supportId로 Support 객체 조회
    private Support getSupport (long supportId) {
        return supportRepository.findById(supportId)
                .orElseThrow(() -> new CustomException(ErrorCodeType.SUPPORT_NOT_FOUND));
    }

}
