package com.chungjin.wam.domain.qna.service;

import com.chungjin.wam.domain.qna.dto.QnaDto;
import com.chungjin.wam.domain.qna.dto.QnaMapper;
import com.chungjin.wam.domain.qna.entity.Qna;
import com.chungjin.wam.domain.qna.repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class QnaService {

    private final QnaRepository qnaRepository;
    private final QnaMapper qnaMapper;

    /**
     * QnA 생성
     * */
    public void createQna(QnaDto qnaDto) {
        Qna qna = qnaMapper.toEntity(qnaDto);
        qnaRepository.save(qna);
    }

    /**
     * QnA 조회
     * */
    public QnaDto readQna(Long qnaId) {
        Qna qna = qnaRepository.findById(qnaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 QnA 입니다."));
                //.orElseThrow(() -> new ResponseStatusException(ErrorCode.NOT_FOUND));
        return qnaMapper.toDto(qna);
    }

    /**
     * QnA List 조회 (Pagination)
     * */
    public List<QnaDto> readAllQna(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Qna> qnaPage = qnaRepository.findAll(pageable);
        List<Qna> qnas = qnaPage.getContent();
        return qnaMapper.toDtoList(qnas);
    }

    /**
     * QnA 수정
     * */
    public void updateQna(Long qnaId, QnaDto updateQnaDto) {
        Qna qna = qnaRepository.findById(qnaId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 Qna 입니다."));
        qnaMapper.updateFromDto(updateQnaDto, qna);
    }

    /**
     * QnA 삭제
     * */
    public void deleteQna(Long qnaId) {
        Qna qna = qnaRepository.findById(qnaId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 Qna 입니다."));
        qnaRepository.delete(qna);
    }

}
