package com.chungjin.wam.domain.admin.service;

import com.chungjin.wam.domain.member.entity.Member;
import com.chungjin.wam.domain.member.repository.MemberRepository;
import com.chungjin.wam.domain.qna.entity.Qna;
import com.chungjin.wam.domain.qna.repository.QnaRepository;
import com.chungjin.wam.domain.support.entity.Support;
import com.chungjin.wam.domain.support.repository.SupportRepository;
import com.chungjin.wam.global.exception.CustomException;
import com.chungjin.wam.global.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.chungjin.wam.global.exception.error.ErrorCodeType.*;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {

    private final MemberRepository memberRepository;
    private final QnaRepository qnaRepository;
    private final SupportRepository supportRepository;

    private final S3Service s3Service;

    /**
     * 회원 탈퇴
     * @param memberId
     */
    public void deleteMember(Long memberId) {
        //memberId로 Member 객체 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        //DB에서 영구 삭제
        memberRepository.delete(member);
    }

    /**
     * QnA 삭제
     * @param qnaId
     */
    public void deleteQna(Long qnaId) {
        //qnaId로 QnA 객체 조회
        Qna qna = qnaRepository.findById(qnaId)
                .orElseThrow(() -> new CustomException(QNA_NOT_FOUND));

        //DB에서 영구 삭제
        qnaRepository.delete(qna);
    }

    /**
     * 후원 삭제
     * @param supportId
     */
    public void deleteSupport(Long supportId) {
        //supportId로 support 객체 가져오기
        Support support = supportRepository.findById(supportId)
                .orElseThrow(() -> new CustomException(SUPPORT_NOT_FOUND));

        //S3에서 파일 삭제
        deleteFileAtS3(support.getFirstImg());
        //DB에서 영구 삭제
        supportRepository.delete(support);
    }

    //파일 삭제 메소드
    public void deleteFileAtS3(String fileName) {
        if(fileName != null) {
            s3Service.deleteImage(fileName);
        }
    }

}
