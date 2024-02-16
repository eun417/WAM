package com.chungjin.wam.domain.support.service;

import com.chungjin.wam.domain.member.entity.Member;
import com.chungjin.wam.domain.member.repository.MemberRepository;
import com.chungjin.wam.domain.support.dto.SupportDto;
import com.chungjin.wam.domain.support.dto.SupportMapper;
import com.chungjin.wam.domain.support.dto.request.SupportRequestDto;
import com.chungjin.wam.domain.support.dto.request.UpdateSupportRequestDto;
import com.chungjin.wam.domain.support.dto.response.CommentDto;
import com.chungjin.wam.domain.support.dto.response.SupportDetailDto;
import com.chungjin.wam.domain.support.entity.Support;
import com.chungjin.wam.domain.support.entity.SupportStatus;
import com.chungjin.wam.domain.support.repository.SupportRepository;
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
public class SupportService {

    private final SupportRepository supportRepository;
    private final MemberRepository memberRepository;
    private final CommentService commentService;
    private final SupportMapper supportMapper;

    /**
     * 후원 생성
     */
    public void createSupport(String email, SupportRequestDto supportReq) {
        //이메일로 사용자 확인
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        //Dto -> Entity
        Support support = Support.builder()
                .title(supportReq.getTitle())
                .animalSubjects(supportReq.getAnimalSubjects())
                .goalAmount(supportReq.getGoalAmount())
                .startDate(supportReq.getStartDate())
                .endDate(supportReq.getEndDate())
                .firstImg(supportReq.getFirstImg())
                .subheading(supportReq.getSubheading())
                .content(supportReq.getContent())
                .commentCheck(supportReq.getCommentCheck())
                .member(member)
                .supportStatus(SupportStatus.START)
                .build();

        //DB에 저장
        supportRepository.save(support);
    }

    /**
     * 후원 조회
     */
    public SupportDetailDto readSupport(Long supportId) {
        //supportId로 support 객체 가져오기
        Support support = getSupport(supportId);
        //supportId로 comment List 가져오기
        List<CommentDto> comments = commentService.findAllComment(supportId);

        //Entity -> Dto
        return SupportDetailDto.builder()
                .supportId(support.getSupportId())
                .animalSubjects(support.getAnimalSubjects())
                .title(support.getTitle())
                .goalAmount(support.getGoalAmount())
                .supportStatus(support.getSupportStatus())
                .startDate(support.getStartDate())
                .endDate(support.getEndDate())
                .firstImg(support.getFirstImg())
                .subheading(support.getSubheading())
                .content(support.getContent())
                .supportLike(support.getSupportLike())
                .supportAmount(support.getSupportAmount())
                .comments(comments)
                .build();
    }

    /**
     * 후원 List 조회 (Pagination)
     */
    public List<SupportDto> readAllSupport(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Support> supportPage = supportRepository.findAll(pageable);
        List<Support> supports = supportPage.getContent();
        return supportMapper.toDtoList(supports);
    }

    /**
     * 후원 수정
     */
    public void updateSupport(String email, Long supportId, UpdateSupportRequestDto updateSupportReq) {
        //supportId로 support 객체 가져오기
        Support support = getSupport(supportId);

        //로그인한 사용자가 작성자가 아닌 경우 에러 발생
        if(!email.equals(support.getMember().getEmail())) throw new ResponseStatusException(FORBIDDEN, "접근권한이 없습니다.");

        //MapStruct로 수정
        supportMapper.updateFromDto(updateSupportReq, support);
    }

    /**
     * 후원 삭제
     */
    public void deleteSupport(String email, Long supportId) {
        //supportId로 support 객체 가져오기
        Support support = getSupport(supportId);

        //로그인한 사용자가 작성자가 아닌 경우 에러 발생
        if(!email.equals(support.getMember().getEmail())) throw new ResponseStatusException(FORBIDDEN, "접근권한이 없습니다.");

        //DB에서 영구 삭제
        supportRepository.delete(support);
    }

    /**
     * supportId로 Support 객체 조회
     */
    private Support getSupport (long supportId) {
        return supportRepository.findById(supportId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "존재하지 않는 후원 입니다."));
    }

}
