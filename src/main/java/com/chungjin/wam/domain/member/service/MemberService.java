package com.chungjin.wam.domain.member.service;

import com.chungjin.wam.domain.member.dto.MemberMapper;
import com.chungjin.wam.domain.member.dto.MemberSupportIdDto;
import com.chungjin.wam.domain.member.dto.request.UpdateMemberRequestDto;
import com.chungjin.wam.domain.member.dto.response.MemberDto;
import com.chungjin.wam.domain.member.dto.response.MyQnaResponseDto;
import com.chungjin.wam.domain.member.dto.response.MySupportResponseDto;
import com.chungjin.wam.domain.member.entity.Member;
import com.chungjin.wam.domain.member.repository.MemberRepository;
import com.chungjin.wam.domain.qna.dto.QnaMapper;
import com.chungjin.wam.domain.qna.entity.Qna;
import com.chungjin.wam.domain.qna.repository.QnaRepository;
import com.chungjin.wam.domain.support.dto.SupportMapper;
import com.chungjin.wam.domain.support.entity.Support;
import com.chungjin.wam.domain.support.repository.SupportLikeRepository;
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

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final QnaRepository qnaRepository;
    private final SupportRepository supportRepository;
    private final SupportLikeRepository supportLikeRepository;
    private final MemberMapper memberMapper;
    private final QnaMapper qnaMapper;
    private final SupportMapper supportMapper;

    /**
     * 로그인 회원의 email로 회원 정보 조회
     */
    public MemberDto getMemberProfile(Long memberId) {
        //email로 Member 객체 가져오기
        Member member = getMember(memberId);
        //Entity -> Dto
        return memberMapper.toDto(member);
    }

    /**
     * 회원 수정
     */
    public void updateMember(Long memberId, UpdateMemberRequestDto updateMembmerDto) {
        //email로 Member 객체 가져오기
        Member member = getMember(memberId);

        //로그인한 사용자가 마이페이지의 회원이 아닌 경우 에러 발생
        if(!memberId.equals(member.getMemberId())) throw new ResponseStatusException(FORBIDDEN, "접근권한이 없습니다.");

        //MapStruct로 수정
        memberMapper.updateFromDto(updateMembmerDto, member);
    }

    /**
     * 회원 탈퇴
     */
    public void deleteMember(Long memberId) {
        //email로 Member 객체 가져오기
        Member member = getMember(memberId);

        //로그인한 사용자가 마이페이지의 회원이 아닌 경우 에러 발생
        if(!memberId.equals(member.getMemberId())) throw new ResponseStatusException(FORBIDDEN, "접근권한이 없습니다.");

        //DB에서 영구 삭제
        memberRepository.delete(member);
    }

    /**
     * 자신이 작성한 QnA List 조회 (Pagination)
     */
    public List<MyQnaResponseDto> getMyQna(Long memberId, int page) {
        //한 페이지당 10개 항목 표시
        Pageable pageable = PageRequest.of(page, 10);
        //Qna를 페이지별 조회
        Page<Qna> qnaPage = qnaRepository.findByMemberId(memberId, pageable);
        //현재 페이지의 Qna 목록
        List<Qna> qnas = qnaPage.getContent();

        //EntityList -> DtoList
        return qnaMapper.toMyQnaDtoList(qnas);
    }

    /**
     * 자신이 작성한 후원 List 조회 (Pagination)
     */
    public List<MySupportResponseDto> getMySupport(Long memberId, int page) {
        //한 페이지당 10개 항목 표시
        Pageable pageable = PageRequest.of(page, 10);
        //후원을 페이지별 조회
        Page<Support> supportPage = supportRepository.findByMemberId(memberId, pageable);
        //현재 페이지의 후원 목록
        List<Support> supports = supportPage.getContent();

        //EntityList -> DtoList
        return supportMapper.toMySupportDtoList(supports);
    }

    /**
     * 자신이 추가한 좋아요 조회 (Pagination)
     */
    public List<MySupportResponseDto> getMyLike(Long memberId, int page) {
        //한 페이지당 10개 항목 표시
        Pageable pageable = PageRequest.of(page, 10);
        //후원을 페이지별 조회
        Page<Support> supportPage = supportLikeRepository.findByMemberId(memberId, pageable);
        //현재 페이지의 후원 목록
        List<Support> supports = supportPage.getContent();

        //EntityList -> DtoList
        return supportMapper.toMySupportDtoList(supports);
    }

    /**
     * memberId로 Member 객체 조회
     */
    private Member getMember (Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
    }

}
