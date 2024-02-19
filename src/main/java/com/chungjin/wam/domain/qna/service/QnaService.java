package com.chungjin.wam.domain.qna.service;

import com.chungjin.wam.domain.member.entity.Member;
import com.chungjin.wam.domain.member.repository.MemberRepository;
import com.chungjin.wam.domain.qna.dto.QnaDto;
import com.chungjin.wam.domain.qna.dto.QnaMapper;
import com.chungjin.wam.domain.qna.dto.request.QnaAnswerRequestDto;
import com.chungjin.wam.domain.qna.dto.request.QnaRequestDto;
import com.chungjin.wam.domain.qna.dto.request.UpdateQnaRequestDto;
import com.chungjin.wam.domain.qna.entity.Qna;
import com.chungjin.wam.domain.qna.entity.QnaCheck;
import com.chungjin.wam.domain.qna.repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class QnaService {

    private final QnaRepository qnaRepository;
    private final MemberRepository memberRepository;
    private final QnaMapper qnaMapper;

    /**
     * QnA 생성
     */
    public void createQna(Long memberId, QnaRequestDto qnaReq) {
        //이메일로 사용자 확인
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        //Dto -> Entity
        Qna qna = Qna.builder()
                .title(qnaReq.getTitle())
                .content(qnaReq.getContent())
                .qnaCheck(QnaCheck.CHECKING)
                .member(member)
                .build();

        //DB에 저장
        qnaRepository.save(qna);
    }

    /**
     * QnA 조회
     */
    public QnaDto readQna(Long qnaId) {
        //qnaId로 QnA 확인
        Qna qna = getQna(qnaId);
        //Entity -> Dto
        return qnaMapper.toDto(qna);
    }

    /**
     * QnA List 조회 (Pagination)
     */
    public List<QnaDto> readAllQna(int page) {
        //한 페이지당 10개 항목 표시
        Pageable pageable = PageRequest.of(page, 10);
        //Qna를 페이지별 조회
        Page<Qna> qnaPage = qnaRepository.findAll(pageable);
        //현재 페이지의 Qna 목록
        List<Qna> qnas = qnaPage.getContent();
        //EntityList -> DtoList
        return qnaMapper.toDtoList(qnas);
    }

    /**
     * QnA 수정
     */
    public void updateQna(Long memberId, Long qnaId, UpdateQnaRequestDto updateQnaReq) {
        //qnaId로 QnA 확인
        Qna qna = getQna(qnaId);

        //로그인한 사용자가 작성자가 아닌 경우 에러 발생
        if(!memberId.equals(qna.getMember().getMemberId())) throw new ResponseStatusException(FORBIDDEN, "접근권한이 없습니다.");

        //MapStruct로 수정
        qnaMapper.updateFromUpdateDto(updateQnaReq, qna);
    }

    /**
     * QnA 삭제
     */
    public void deleteQna(Long memberId, Long qnaId) {
        //qnaId로 QnA 확인
        Qna qna = getQna(qnaId);

        //로그인한 사용자가 작성자가 아닌 경우 에러 발생
        if(!memberId.equals(qna.getMember().getMemberId())) throw new ResponseStatusException(FORBIDDEN, "접근권한이 없습니다.");

        //DB에서 영구 삭제
        qnaRepository.delete(qna);
    }

    /**
     * QnA 답변 등록
     */
    public void updateQnaAnswer(Long qnaId, QnaAnswerRequestDto qnaAnswerReq) {
        //qnaId로 QnA 확인
        Qna qna = getQna(qnaId);

        //MapStruct로 수정
        qnaMapper.updateFromAnswerDto(qnaAnswerReq, qna);
    }

    /**
     * qnaId로 Qna 객체 조회
     */
    private Qna getQna (long qnaId) {
        return qnaRepository.findById(qnaId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "존재하지 않는 Qna 입니다."));
    }

}
