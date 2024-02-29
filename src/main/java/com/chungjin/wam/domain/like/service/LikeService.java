package com.chungjin.wam.domain.like.service;

import com.chungjin.wam.domain.member.entity.Member;
import com.chungjin.wam.domain.member.repository.MemberRepository;
import com.chungjin.wam.domain.support.entity.Support;
import com.chungjin.wam.domain.like.entity.SupportLike;
import com.chungjin.wam.domain.support.repository.SupportLikeRepository;
import com.chungjin.wam.domain.support.repository.SupportRepository;
import com.chungjin.wam.global.exception.CustomException;
import com.chungjin.wam.global.exception.error.ErrorCodeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {

    private final SupportLikeRepository supportLikeRepository;
    private final MemberRepository memberRepository;
    private final SupportRepository supportRepository;

    /**
     * 좋아요 생성
     */
    public void createLike(Long memberId, Long supportId) {
        //memberId로 Member 객체 가져오기
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCodeType.MEMBER_NOT_FOUND));
        //supportId로 support 객체 가져오기
        Support support = getSupport(supportId);

        //좋아요 증가
        support.upLike(support.getSupportLike());

        //Entity 생성
        SupportLike like = SupportLike.builder()
                .support(support)
                .member(member)
                .build();

        //DB에 저장
        supportLikeRepository.save(like);
    }

    /**
     * 좋아요 삭제
     */
    public void deleteLike(Long memberId, Long supportId, Long supportLikeId) {
        //supportId로 support 객체 가져오기
        Support support = getSupport(supportId);
        //supportLikeId로 supportLike 객체 가져오기
        SupportLike supportLike = supportLikeRepository.findById(supportLikeId)
                .orElseThrow(() -> new CustomException(ErrorCodeType.SUPPORT_LIKE_NOT_FOUND));

        //로그인한 사용자가 좋아요 생성한 사람이 아닌 경우 에러 발생
        if(!memberId.equals(support.getMember().getMemberId())) throw new CustomException(ErrorCodeType.FORBIDDEN);

        //좋아요 감소
        support.downLike(support.getSupportLike());

        //DB에서 영구 삭제
        supportLikeRepository.delete(supportLike);
    }

    /**
     * supportId로 Support 객체 조회
     */
    private Support getSupport (long supportId) {
        return supportRepository.findById(supportId)
                .orElseThrow(() -> new CustomException(ErrorCodeType.SUPPORT_NOT_FOUND));
    }

}
