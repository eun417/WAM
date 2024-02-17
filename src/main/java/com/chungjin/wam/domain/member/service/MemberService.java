package com.chungjin.wam.domain.member.service;

import com.chungjin.wam.domain.member.dto.MemberMapper;
import com.chungjin.wam.domain.member.dto.request.UpdateMemberRequestDto;
import com.chungjin.wam.domain.member.dto.response.MemberDto;
import com.chungjin.wam.domain.member.entity.Member;
import com.chungjin.wam.domain.member.repository.MemberRepository;
import com.chungjin.wam.domain.qna.entity.Qna;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

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
     * 로그인 회원의 email로 Member 객체 조회
     */
    private Member getMember (String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
    }

}
