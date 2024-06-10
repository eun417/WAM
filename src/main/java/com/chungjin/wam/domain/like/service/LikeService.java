package com.chungjin.wam.domain.like.service;

import com.chungjin.wam.domain.member.entity.Member;
import com.chungjin.wam.domain.support.entity.Support;
import com.chungjin.wam.domain.like.entity.SupportLike;
import com.chungjin.wam.domain.like.repository.SupportLikeRepository;
import com.chungjin.wam.global.exception.CustomException;
import com.chungjin.wam.global.util.EntityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.chungjin.wam.global.exception.error.ErrorCodeType.*;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {

    private final SupportLikeRepository supportLikeRepository;

    private final EntityUtils entityUtils;

    /**
     * 좋아요 생성
     */

    public void createLike(Long memberId, Long supportId) {
        //memberId로 Member 객체 가져오기
        Member member = entityUtils.getMember(memberId);
        //supportId로 support 객체 가져오기
        Support support = entityUtils.getSupport(supportId);

        //좋아요 증가
        support.upLike();

        //Entity 생성
        SupportLike like = SupportLike.builder()
                .support(support)
                .member(member)
                .build();

        //DB에 저장
        supportLikeRepository.save(like);
    }

    /**
     * 좋아요 상태 조회
     */
    @Transactional(readOnly = true)
    public boolean readLikeStatus(Long memberId, Long supportId) {
        //memberId, supportId로 좋아요 조회
        return supportLikeRepository.existsBySupport_SupportIdAndMember_MemberId(supportId, memberId);
    }

    /**
     * 좋아요 삭제
     */
    public void deleteLike(Long memberId, Long supportId) {
        //supportId로 support 객체 가져오기
        Support support = entityUtils.getSupport(supportId);
        //supportLikeId로 supportLike 객체 가져오기
        SupportLike supportLike = supportLikeRepository.findBySupport_SupportIdAndMember_MemberId(supportId, memberId)
                .orElseThrow(() -> new CustomException(SUPPORT_LIKE_NOT_FOUND));

        //로그인한 사용자가 좋아요 생성한 사람이 아닌 경우 에러 발생
        entityUtils.checkWriter(supportLike.getMember().getMemberId(), memberId);

        //좋아요 감소
        support.downLike();

        //DB에서 영구 삭제
        supportLikeRepository.delete(supportLike);
    }

}
