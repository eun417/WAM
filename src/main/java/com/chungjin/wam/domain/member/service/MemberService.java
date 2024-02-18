package com.chungjin.wam.domain.member.service;

import com.chungjin.wam.domain.member.dto.MemberMapper;
import com.chungjin.wam.domain.member.dto.request.UpdateMemberRequestDto;
import com.chungjin.wam.domain.member.dto.response.MemberDto;
import com.chungjin.wam.domain.member.dto.response.MyQnaResponseDto;
import com.chungjin.wam.domain.member.entity.Member;
import com.chungjin.wam.domain.member.repository.MemberRepository;
import com.chungjin.wam.domain.qna.dto.QnaMapper;
import com.chungjin.wam.domain.qna.entity.Qna;
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

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final QnaRepository qnaRepository;
    private final MemberMapper memberMapper;
    private final QnaMapper qnaMapper;

    /**
     * 로그인 회원의 email로 회원 정보 조회
     */
    public MemberDto getMemberProfile(String email) {
        //email로 Member 객체 가져오기
        Member member = getMember(email);
        //Entity -> Dto
        return memberMapper.toDto(member);
    }

    /**
     * 회원 수정
     */
    public void updateMember(String email, UpdateMemberRequestDto updateMembmerDto) {
        //email로 Member 객체 가져오기
        Member member = getMember(email);

        //로그인한 사용자가 마이페이지의 회원이 아닌 경우 에러 발생
        if(!email.equals(member.getEmail())) throw new ResponseStatusException(FORBIDDEN, "접근권한이 없습니다.");

        //MapStruct로 수정
        memberMapper.updateFromDto(updateMembmerDto, member);
    }

    /**
     * 회원 탈퇴
     */
    public void deleteMember(String email) {
        //email로 Member 객체 가져오기
        Member member = getMember(email);

        //로그인한 사용자가 마이페이지의 회원이 아닌 경우 에러 발생
        if(!email.equals(member.getEmail())) throw new ResponseStatusException(FORBIDDEN, "접근권한이 없습니다.");

        //DB에서 영구 삭제
        memberRepository.delete(member);
    }

    /**
     * 자신이 작성한 QnA List 조회 (Pagination)
     */
    public List<MyQnaResponseDto> getMyQna(String email, int page) {
        //email로 Member 객체 가져오기
        Member member = getMember(email);

        //한 페이지당 10개 항목 표시
        Pageable pageable = PageRequest.of(page, 10);
        //Qna를 페이지별 조회
        Page<Qna> qnaPage = qnaRepository.findByMemberId(member.getMemberId(), pageable);
        //현재 페이지의 Qna 목록
        List<Qna> qnas = qnaPage.getContent();

        //EntityList -> DtoList
        return qnaMapper.toMyQnaDtoList(qnas);
    }

    /**
     * 로그인 회원의 email로 Member 객체 조회
     */
    private Member getMember (String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
    }

}
