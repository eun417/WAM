package com.chungjin.wam.domain.member.service;

import com.chungjin.wam.domain.auth.service.AuthService;
import com.chungjin.wam.domain.member.dto.MemberMapper;
import com.chungjin.wam.domain.member.dto.request.UpdateMemberRequestDto;
import com.chungjin.wam.domain.member.dto.request.UpdatePwRequestDto;
import com.chungjin.wam.domain.member.dto.response.MemberResponseDto;
import com.chungjin.wam.domain.member.dto.response.MyLikeResponseDto;
import com.chungjin.wam.global.common.PageResponse;
import com.chungjin.wam.domain.member.entity.Authority;
import com.chungjin.wam.domain.member.entity.Member;
import com.chungjin.wam.domain.member.repository.MemberRepository;
import com.chungjin.wam.domain.qna.entity.Qna;
import com.chungjin.wam.domain.qna.repository.QnaRepository;
import com.chungjin.wam.domain.support.entity.Support;
import com.chungjin.wam.domain.support.repository.SupportLikeRepository;
import com.chungjin.wam.domain.support.repository.SupportRepository;
import com.chungjin.wam.global.exception.CustomException;
import com.chungjin.wam.global.util.EntityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.chungjin.wam.domain.member.entity.Authority.ROLE_USER;
import static com.chungjin.wam.global.exception.error.ErrorCodeType.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final QnaRepository qnaRepository;
    private final SupportRepository supportRepository;
    private final SupportLikeRepository supportLikeRepository;

    private final AuthService authService;

    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;
    private final EntityUtils entityUtils;

    /**
     * 로그인 회원의 email로 회원 정보 조회
     */
    public MemberResponseDto getMemberProfile(Long memberId) {
        //memberId로 Member 객체 가져오기
        Member member = entityUtils.getMember(memberId);
        //Entity -> Dto
        return memberMapper.toDto(member);
    }

    /**
     * 회원 수정
     */
    @Transactional
    public void updateMember(Long memberId, UpdateMemberRequestDto updateMemberReq) {
        //memberId로 Member 객체 가져오기
        Member member = entityUtils.getMember(memberId);

        //이름, 휴대폰 번호 모두 입력하면 GUEST인 사용자의 권한을 USER로 변경
        if (updateMemberReq.getName() != null && updateMemberReq.getPhoneNumber() != null && member.getAuthority() == Authority.ROLE_GUEST) {
            member.updateAuthority(ROLE_USER);
        }

        //MapStruct로 수정
        memberMapper.updateFromDto(updateMemberReq, member);
    }

    /**
     * 비밀번호 변경
     */
    @Transactional
    public void updatePw(Long memberId, UpdatePwRequestDto updatePwReq) {
        //memberId로 Member 객체 가져오기
        Member member = entityUtils.getMember(memberId);

        //입력한 비밀번호가 암호화된 비밀번호와 맞는지 확인
        validatePassword(updatePwReq.getNowPassword(), member.getPassword());

        //비밀번호 암호화 후 변경
        member.updatePw(passwordEncoder.encode(updatePwReq.getNewPassword()));
    }

    /**
     * 회원 탈퇴
     */
    @Transactional
    public void deleteMember(Long memberId, String password) {
        //memberId로 로그인 한 Member 객체 가져오기
        Member member = entityUtils.getMember(memberId);

        //입력한 비밀번호가 암호화된 비밀번호와 맞는지 확인
        validatePassword(password, member.getPassword());

        //회원 탈퇴
        memberRepository.delete(member);

        //로그아웃 처리
        authService.logout(memberId.toString());
    }

    /**
     * 자신이 작성한 QnA List 조회 (Pagination)
     */
    public PageResponse getMyQna(Long memberId, int pageNo) {
        //한 페이지당 10개 항목 표시
        Pageable pageable = PageRequest.of(pageNo, 10, Sort.by("qnaId").descending());
        //Qna를 페이지별 조회
        Page<Qna> qnaPage = qnaRepository.findByMemberId(memberId, pageable);
        //현재 페이지의 Qna 목록
        List<Qna> qnas = qnaPage.getContent();
        //EntityList -> DtoList
        List<Object> qnaDtos = new ArrayList<>();
        for (Qna qna : qnas) {
            qnaDtos.add(memberMapper.toDto(qna)); //Qna를 MyQnaDto로 변환하여 리스트에 추가
        }
        return PageResponse.createPageResponse(qnaDtos, qnaPage, pageNo);
    }

    /**
     * 자신이 작성한 후원 List 조회 (Pagination)
     */
    public PageResponse getMySupport(Long memberId, int pageNo) {
        //한 페이지당 10개 항목 표시
        Pageable pageable = PageRequest.of(pageNo, 10, Sort.by("supportId").descending());
        //후원을 페이지별 조회
        Page<Support> supportPage = supportRepository.findByMemberId(memberId, pageable);
        //현재 페이지의 후원 목록
        List<Support> supports = supportPage.getContent();
        //EntityList -> DtoList
        List<Object> supportDtos = new ArrayList<>();
        for (Support support : supports) {
            supportDtos.add(memberMapper.toDto(support)); //Support를 MySupportDto로 변환하여 리스트에 추가
        }
        return PageResponse.createPageResponse(supportDtos, supportPage, pageNo);
    }

    /**
     * 자신이 추가한 좋아요 조회 (Pagination)
     */
    public PageResponse getMyLike(Long memberId, int pageNo) {
        //한 페이지당 10개 항목 표시
        Pageable pageable = PageRequest.of(pageNo, 10, Sort.by("supportLikeId").descending());
        //후원을 페이지별 조회
        Page<Support> likePage = supportLikeRepository.findByMemberId(memberId, pageable);
        //현재 페이지의 후원 목록
        List<Support> supports = likePage.getContent();
        //EntityList -> DtoList
        List<Object> likeDtos = new ArrayList<>();
        for (Support support : supports) {
            likeDtos.add(MyLikeResponseDto.toDto(support)); //Support를 MyLikeDto로 변환하여 리스트에 추가
        }
        return PageResponse.createPageResponse(likeDtos, likePage, pageNo);
    }

    /**
     * 모든 회원 조회
     */
    public PageResponse readAllMember(int pageNo) {
        //한 페이지당 10개 항목 표시
        Pageable pageable = PageRequest.of(pageNo, 10, Sort.by("memberId").descending());
        //회원을 페이지별 조회
        Page<Member> memberPage = memberRepository.findAll(pageable);
        //현재 페이지의 Member 목록
        List<Member> members = memberPage.getContent();
        //EntityList -> DtoList
        List<Object> memberDtos = new ArrayList<>();
        for (Member member : members) {
            memberDtos.add(memberMapper.toDto(member)); //Member를 MemberDto로 변환하여 리스트에 추가
        }
        return PageResponse.createPageResponse(memberDtos, memberPage, pageNo);
    }

    //입력한 비밀번호가 암호화된 비밀번호와 맞는지 확인하는 함수
    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            log.info("비밀번호 일치 여부: {}", passwordEncoder.matches(rawPassword, encodedPassword));
            throw new CustomException(INVALID_PASSWORD);
        }
    }

}
