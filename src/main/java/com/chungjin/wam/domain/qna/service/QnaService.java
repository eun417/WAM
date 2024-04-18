package com.chungjin.wam.domain.qna.service;

import com.chungjin.wam.domain.member.entity.Member;
import com.chungjin.wam.domain.member.repository.MemberRepository;
import com.chungjin.wam.domain.member.service.MemberService;
import com.chungjin.wam.domain.qna.dto.QnaMapper;
import com.chungjin.wam.domain.qna.dto.request.QnaAnswerRequestDto;
import com.chungjin.wam.domain.qna.dto.request.QnaRequestDto;
import com.chungjin.wam.domain.qna.dto.request.UpdateQnaRequestDto;
import com.chungjin.wam.domain.qna.dto.response.QnaDetailDto;
import com.chungjin.wam.domain.qna.dto.response.QnaResponseDto;
import com.chungjin.wam.domain.qna.entity.Qna;
import com.chungjin.wam.domain.qna.repository.QnaRepository;
import com.chungjin.wam.global.common.PageResponse;
import com.chungjin.wam.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.chungjin.wam.domain.qna.entity.QnaCheck.CHECKING;
import static com.chungjin.wam.global.exception.error.ErrorCodeType.*;

@Service
@RequiredArgsConstructor
@Transactional
public class QnaService {

    private final QnaRepository qnaRepository;
    private final MemberRepository memberRepository;

    private final MemberService memberService;

    private final QnaMapper qnaMapper;

    /**
     * QnA 생성
     */
    public void createQna(Long memberId, QnaRequestDto qnaReq) {
        //memberId로 Member 객체 가져오기
        Member member = getMember(memberId);

        //Dto -> Entity
        Qna qna = Qna.builder()
                .title(qnaReq.getTitle())
                .content(qnaReq.getContent())
                .qnaCheck(CHECKING)
                .member(member)
                .build();

        //DB에 저장
        qnaRepository.save(qna);
    }

    //memberId로 Member 객체 조회하는 함수
    private Member getMember (Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
    }

    /**
     * QnA 상세 조회
     */
    public QnaDetailDto readQna(Long qnaId) {
        //qnaId로 QnA 객체 가져오기
        Qna qna = getQna(qnaId);

        //조회수 증가
        qna.upViewCount(qna.getViewCount());

        //Entity -> Dto
        return QnaDetailDto.builder()
                .qnaId(qna.getQnaId())
                .memberId(memberService.getMemberIdForMember(qna.getMember()))
                .nickname(memberService.getNicknameForMember(qna.getMember()))
                .title(qna.getTitle())
                .content(qna.getContent())
                .createDate(qna.getCreateDate())
                .viewCount(qna.getViewCount())
                .answer(qna.getAnswer())
                .answerDate(qna.getAnswerDate())
                .qnaCheck(qna.getQnaCheck())
                .build();
    }

    /**
     * QnA List 조회 (Pagination)
     */
    public PageResponse readAllQna(int pageNo) {
        //한 페이지당 10개 항목 표시
        Pageable pageable = PageRequest.of(pageNo, 10, Sort.by("qnaId").descending());
        return getQnaPageResponse(qnaRepository.findAll(pageable), pageNo);
    }

    /**
     * QnA 수정
     */
    public void updateQna(Long memberId, Long qnaId, UpdateQnaRequestDto updateQnaReq) {
        //qnaId로 QnA 객체 가져오기
        Qna qna = getQna(qnaId);

        //로그인한 사용자가 작성자가 아닌 경우 에러 발생
        checkQnaWriter(qna.getMember().getMemberId(), memberId);

        //MapStruct 로 수정
        qnaMapper.updateFromUpdateDto(updateQnaReq, qna);
    }

    /**
     * QnA 삭제
     */
    public void deleteQna(Long memberId, Long qnaId) {
        //qnaId로 QnA 객체 가져오기
        Qna qna = getQna(qnaId);

        //로그인한 사용자가 작성자가 아닌 경우 에러 발생
        checkQnaWriter(qna.getMember().getMemberId(), memberId);

        //DB 에서 영구 삭제
        qnaRepository.delete(qna);
    }

    ////로그인한 사용자가 작성자인지 확인하는 함수
    private void checkQnaWriter(Long writerId, Long loginMemberId) {
        if (!loginMemberId.equals(writerId)) {
            throw new CustomException(FORBIDDEN);
        }
    }

    /**
     * QnA 답변 등록
     */
    public void updateQnaAnswer(Long qnaId, QnaAnswerRequestDto qnaAnswerReq) {
        //qnaId로 QnA 객체 가져오기
        Qna qna = getQna(qnaId);

        //MapStruct 로 수정
        qnaMapper.updateFromAnswerDto(qnaAnswerReq, qna);
    }

    /**
     * 검색 - 제목, 내용, 작성자
     */
    public PageResponse searchQna(String keyword, int pageNo) {
        //한 페이지당 10개 항목 표시
        Pageable pageable = PageRequest.of(pageNo, 10, Sort.by("qnaId").descending());
        return getQnaPageResponse(qnaRepository.findByTitleOrContentOrNicknameContaining(keyword, pageable), pageNo);
    }

    //qnaId로 Qna 객체 조회하는 함수
    private Qna getQna (long qnaId) {
        return qnaRepository.findById(qnaId)
                .orElseThrow(() -> new CustomException(QNA_NOT_FOUND));
    }

    //Pagination 결과 함수
    private PageResponse getQnaPageResponse(Page<Qna> qnaPage, int pageNo) {
        //현재 페이지의 Qna 목록
        List<Qna> qnas = qnaPage.getContent();
        //EntityList -> DtoList
        List<Object> qnaDtos = qnas.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return PageResponse.builder()
                .content(qnaDtos)   //Qna 목록
                .pageNo(pageNo) //현재 페이지 번호
                .pageSize(qnaPage.getSize()) //페이지당 항목 수
                .totalElements(qnaPage.getTotalElements())   //전체 Qna 수
                .totalPages(qnaPage.getTotalPages()) //전체 페이지 수
                .last(qnaPage.isLast())  //마지막 페이지 여부
                .build();
    }

    //Entity -> Dto
    public QnaResponseDto convertToDto(Qna qna) {
        return QnaResponseDto.builder()
                .qnaId(qna.getQnaId())
                .title(qna.getTitle())
                .content(qna.getContent())
                .createDate(qna.getCreateDate())
                .viewCount(qna.getViewCount())
                .qnaCheck(qna.getQnaCheck())
                .nickname(memberService.getNicknameForMember(qna.getMember()))
                .build();
    }

}
