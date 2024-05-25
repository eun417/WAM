package com.chungjin.wam.global.util;

import com.chungjin.wam.domain.member.entity.Member;
import com.chungjin.wam.domain.member.repository.MemberRepository;
import com.chungjin.wam.domain.qna.entity.Qna;
import com.chungjin.wam.domain.qna.repository.QnaRepository;
import com.chungjin.wam.domain.support.entity.Support;
import com.chungjin.wam.domain.support.repository.SupportRepository;
import com.chungjin.wam.global.exception.CustomException;
import com.chungjin.wam.global.exception.error.ErrorCodeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.chungjin.wam.global.exception.error.ErrorCodeType.*;

@Component
@RequiredArgsConstructor
public class EntityUtils {

    private final MemberRepository memberRepository;
    private final SupportRepository supportRepository;
    private final QnaRepository qnaRepository;

    //memberId로 Member 객체 조회하는 함수
    public Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
    }

    //supportId로 Support 객체 조회하는 함수
    public Support getSupport (Long supportId) {
        return supportRepository.findById(supportId)
                .orElseThrow(() -> new CustomException(ErrorCodeType.SUPPORT_NOT_FOUND));
    }

    //qnaId로 Qna 객체 조회하는 함수
    public Qna getQna (Long qnaId) {
        return qnaRepository.findById(qnaId)
                .orElseThrow(() -> new CustomException(QNA_NOT_FOUND));
    }

    //로그인한 사용자가 작성자인지 확인하는 함수
    public void checkWriter(Long writerId, Long loginMemberId) {
        if (!loginMemberId.equals(writerId)) {
            throw new CustomException(FORBIDDEN);
        }
    }

    //중복 이메일 확인하는 함수
    public void checkEmailExists(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new CustomException(DUPLICATE_EMAIL);
        }
    }

    //회원이 탈퇴했을 경우 대비하여 memberId 반환하는 함수
    public Long getMemberId(Member member) {
        return (member != null) ? member.getMemberId() : 0L;
    }

    //회원이 탈퇴했을 경우 대비하여 닉네임 반환하는 함수
    public String getNickname(Member member) {
        return (member != null) ? member.getNickname() : "(알 수 없음)";
    }

}
