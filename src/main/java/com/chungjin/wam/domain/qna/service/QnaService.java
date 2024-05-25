package com.chungjin.wam.domain.qna.service;

import com.chungjin.wam.domain.file.service.FileService;
import com.chungjin.wam.domain.member.entity.Member;
import com.chungjin.wam.domain.qna.dto.QnaMapper;
import com.chungjin.wam.domain.qna.dto.request.QnaAnswerRequestDto;
import com.chungjin.wam.domain.qna.dto.request.QnaRequestDto;
import com.chungjin.wam.domain.qna.dto.request.UpdateQnaRequestDto;
import com.chungjin.wam.domain.qna.dto.response.QnaDetailDto;
import com.chungjin.wam.domain.qna.dto.response.QnaResponseDto;
import com.chungjin.wam.domain.qna.entity.Qna;
import com.chungjin.wam.domain.qna.repository.QnaRepository;
import com.chungjin.wam.global.common.PageResponse;
import com.chungjin.wam.global.util.EntityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.chungjin.wam.domain.file.entity.Board.QNA;
import static com.chungjin.wam.domain.qna.entity.QnaCheck.CHECKING;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QnaService {

    private final QnaRepository qnaRepository;

    private final FileService fileService;

    private final QnaMapper qnaMapper;
    private final EntityUtils entityUtils;

    /**
     * QnA 생성
     */
    @Transactional
    public void createQna(Long memberId, QnaRequestDto qnaReq) {
        //memberId로 Member 객체 가져오기
        Member member = entityUtils.getMember(memberId);

        //Dto -> Entity
        Qna qna = Qna.builder()
                .title(qnaReq.getTitle())
                .content(qnaReq.getContent())
                .qnaCheck(CHECKING)
                .member(member)
                .build();

        //DB에 저장
        qnaRepository.save(qna);

        //본문 첨부한 이미지들 DB에 저장
        fileService.saveImages(qnaReq.getFileUrls(), QNA, qna.getQnaId());
    }

    /**
     * QnA 상세 조회
     */
    @Transactional
    public QnaDetailDto readQna(Long qnaId) {
        //qnaId로 QnA 객체 가져오기
        Qna qna = entityUtils.getQna(qnaId);

        //조회수 증가
        qna.upViewCount();

        //Entity -> Dto
        return QnaDetailDto.builder()
                .qnaId(qna.getQnaId())
                .memberId(entityUtils.getMemberId(qna.getMember()))
                .nickname(entityUtils.getNickname(qna.getMember()))
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
    @Transactional
    public void updateQna(Long memberId, Long qnaId, UpdateQnaRequestDto updateQnaReq) {
        //qnaId로 QnA 객체 가져오기
        Qna qna = entityUtils.getQna(qnaId);

        //로그인한 사용자가 작성자가 아닌 경우 에러 발생
        entityUtils.checkWriter(qna.getMember().getMemberId(), memberId);

        //MapStruct 로 수정
        qnaMapper.updateFromUpdateDto(updateQnaReq, qna);
    }

    /**
     * QnA 삭제
     */
    @Transactional
    public void deleteQna(Long memberId, Long qnaId) {
        //qnaId로 QnA 객체 가져오기
        Qna qna = entityUtils.getQna(qnaId);

        //로그인한 사용자가 작성자가 아닌 경우 에러 발생
        entityUtils.checkWriter(qna.getMember().getMemberId(), memberId);

        //DB 에서 영구 삭제
        qnaRepository.delete(qna);
    }

    /**
     * QnA 답변 등록
     */
    @Transactional
    public void updateQnaAnswer(Long qnaId, QnaAnswerRequestDto qnaAnswerReq) {
        //qnaId로 QnA 객체 가져오기
        Qna qna = entityUtils.getQna(qnaId);

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

    //Pagination 결과 함수
    private PageResponse getQnaPageResponse(Page<Qna> qnaPage, int pageNo) {
        //현재 페이지의 Qna 목록
        List<Qna> qnas = qnaPage.getContent();
        //EntityList -> DtoList
        List<Object> qnaDtos = qnas.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return PageResponse.createPageResponse(qnaDtos, qnaPage, pageNo);
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
                .nickname(entityUtils.getNickname(qna.getMember()))
                .build();
    }

}
