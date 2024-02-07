package com.chungjin.wam.domain.support.service;

import com.chungjin.wam.domain.support.dto.SupportDto;
import com.chungjin.wam.domain.support.dto.SupportMapper;
import com.chungjin.wam.domain.support.entity.Support;
import com.chungjin.wam.domain.support.repository.SupportRepository;
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
public class SupportService {

    private final SupportRepository supportRepository;
    private final SupportMapper supportMapper;

    /**
     * 후원 생성
     * */
    public void createSupport(SupportDto supportDto) {
        Support support = supportMapper.toEntity(supportDto);
        supportRepository.save(support);
    }

    /**
     * 후원 조회
     * */
    public SupportDto readSupport(Long supportId) {
        Support support = supportRepository.findById(supportId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 후원 입니다."));
        return supportMapper.toDto(support);
    }

    /**
     * 후원 List 조회 (Pagination)
     * */
    public List<SupportDto> readAllSupport(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Support> supportPage = supportRepository.findAll(pageable);
        List<Support> supports = supportPage.getContent();
        return supportMapper.toDtoList(supports);
    }

    /**
     * 후원 수정
     * */
    public void updateSupport(Long supportId, SupportDto updateSupportDto) {
        Support support = supportRepository.findById(supportId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 후원 입니다."));
        supportMapper.updateFromDto(updateSupportDto, support);
    }

    /**
     * 후원 삭제
     * */
    public void deleteSupport(Long supportId) {
        Support support = supportRepository.findById(supportId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 Qna 입니다."));
        supportRepository.delete(support);
    }

}
