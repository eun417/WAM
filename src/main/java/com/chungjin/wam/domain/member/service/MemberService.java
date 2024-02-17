package com.chungjin.wam.domain.member.service;

import com.chungjin.wam.domain.member.dto.MemberDto;
import com.chungjin.wam.domain.member.dto.MemberMapper;
import com.chungjin.wam.domain.member.entity.Member;
import com.chungjin.wam.domain.member.repository.MemberRepository;
import com.chungjin.wam.domain.qna.dto.QnaDto;
import com.chungjin.wam.domain.qna.entity.Qna;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    /**
     * Id로 회원 조회
     * */
    public MemberDto getMemberById(Long memberId) {
        Member member = memberRepository.findById(memberId).get();
        return memberMapper.toDto(member);
    }

    /**
     * 회원 수정
     * */
    public void updateMember(MemberDto updateMembmerDto) {
        Member member = memberRepository.findById(updateMembmerDto.getMemberId()).get();
        memberMapper.updateFromDto(updateMembmerDto, member);
    }

    /**
     * 회원 삭제
     * */
    public void deleteMember(Long memberId) {
        memberRepository.deleteById(memberId);
    }

}
