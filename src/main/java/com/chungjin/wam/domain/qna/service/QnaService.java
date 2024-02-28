package com.chungjin.wam.domain.qna.service;

import com.chungjin.wam.domain.comment.dto.response.CommentDto;
import com.chungjin.wam.domain.member.entity.Authority;
import com.chungjin.wam.domain.member.entity.Member;
import com.chungjin.wam.domain.member.repository.MemberRepository;
import com.chungjin.wam.domain.qna.dto.QnaMapper;
import com.chungjin.wam.domain.qna.dto.request.QnaAnswerRequestDto;
import com.chungjin.wam.domain.qna.dto.request.QnaRequestDto;
import com.chungjin.wam.domain.qna.dto.request.UpdateQnaRequestDto;
import com.chungjin.wam.domain.qna.dto.response.QnaDetailDto;
import com.chungjin.wam.domain.qna.dto.response.QnaResponseDto;
import com.chungjin.wam.domain.qna.entity.Qna;
import com.chungjin.wam.domain.qna.entity.QnaCheck;
import com.chungjin.wam.domain.qna.repository.QnaRepository;
import com.chungjin.wam.global.exception.CustomException;
import com.chungjin.wam.global.exception.error.ErrorCodeType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        Member member = getMember(memberId);

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
     * QnA 상세 조회
     */
    public QnaDetailDto readQna(Long qnaId) {
        //qnaId로 QnA 객체 가져오기
        Qna qna = getQna(qnaId);

        //조회수 증가
        qna.updateViewCount(qna.getViewCount());

        //Entity -> Dto
        return QnaDetailDto.builder()
                .qnaId(qna.getQnaId())
                .email(qna.getMember().getEmail())
                .title(qna.getTitle())
                .content(qna.getContent())
                .createDate(qna.getCreateDate())
                .viewCount(qna.getViewCount())
                .viewCount(qna.getViewCount())
                .answer(qna.getAnswer())
                .answerDate(qna.getAnswerDate())
                .qnaCheck(qna.getQnaCheck())
                .build();
    }

    /**
     * QnA List 조회 (Pagination)
     */
    public List<QnaResponseDto> readAllQna(int page) {
        //한 페이지당 10개 항목 표시
        Pageable pageable = PageRequest.of(page, 10);
        //Qna를 페이지별 조회
        Page<Qna> qnaPage = qnaRepository.findAll(pageable);
        //현재 페이지의 Qna 목록
        List<Qna> qnas = qnaPage.getContent();
        //EntityList -> DtoList
        return convertToDtoList(qnas);
    }

    /**
     * QnA 수정
     */
    public void updateQna(Long memberId, Long qnaId, UpdateQnaRequestDto updateQnaReq) {
        //qnaId로 QnA 확인
        Qna qna = getQna(qnaId);

        //로그인한 사용자가 작성자가 아닌 경우 에러 발생
        if(!memberId.equals(qna.getMember().getMemberId())) throw new CustomException(ErrorCodeType.FORBIDDEN);

        //MapStruct로 수정
        qnaMapper.updateFromUpdateDto(updateQnaReq, qna);
    }

    /**
     * QnA 삭제
     */
    public void deleteQna(Long memberId, Long qnaId) {
        //qnaId로 QnA 확인
        Qna qna = getQna(qnaId);
        //memberId로 Member 객체 가져오기
        Member member = getMember(memberId);

        //로그인한 사용자가 작성자인 경우 또는 관리자인 경우 삭제 가능
        if(memberId.equals(qna.getMember().getMemberId()) || member.getAuthority().equals(Authority.ROLE_ADMIN)) {
            //DB에서 영구 삭제
            qnaRepository.delete(qna);
        } else {
            //그 외 에러 발생
            throw new CustomException(ErrorCodeType.FORBIDDEN);
        }
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
     * 검색 - 제목+내용
     */
    public List<QnaResponseDto> searchQna(String keyword, int page) {
        //한 페이지당 10개 항목 표시
        Pageable pageable = PageRequest.of(page, 10);
        //검색 키워드를 바탕으로 Qna를 페이지별 조회
        Page<Qna> qnaPage = qnaRepository.findByTitleOrContentContaining(keyword, pageable);
        //현재 페이지의 Qna 목록
        List<Qna> qnas = qnaPage.getContent();
        //EntityList -> DtoList
        return convertToDtoList(qnas);
    }

    /**
     * 검색 - 작성자
     */
    public List<QnaResponseDto> searchQnaWriter(String keyword, int page) {
        //한 페이지당 10개 항목 표시
        Pageable pageable = PageRequest.of(page, 10);
        //검색 키워드를 바탕으로 Qna를 페이지별 조회
        Page<Qna> qnaPage = qnaRepository.findByNicknameContaining(keyword, pageable);
        //현재 페이지의 Qna 목록
        List<Qna> qnas = qnaPage.getContent();
        //EntityList -> DtoList
        return convertToDtoList(qnas);
    }

    /**
     * memberId로 Member 객체 조회
     */
    private Member getMember (Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCodeType.MEMBER_NOT_FOUND));
    }

    /**
     * qnaId로 Qna 객체 조회
     */
    private Qna getQna (long qnaId) {
        return qnaRepository.findById(qnaId)
                .orElseThrow(() -> new CustomException(ErrorCodeType.QNA_NOT_FOUND));
    }

    /**
     * EntityList -> DtoList
     * map()으로 각 QnA를 QnaResponseDto로 변환
     * collect()를 사용하여 변환된 DTO 객체들을 리스트로 수집
     */
    public List<QnaResponseDto> convertToDtoList(List<Qna> qnas) {
        return qnas.stream()
                .map(qna -> QnaResponseDto.builder()
                        .qnaId(qna.getQnaId())
                        .title(qna.getTitle())
                        .nickname(qna.getMember().getNickname())
                        .createDate(qna.getCreateDate())
                        .viewCount(qna.getViewCount())
                        .qnaCheck(qna.getQnaCheck())
                        .build())
                .collect(Collectors.toList());
    }

}
