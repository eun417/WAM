package com.chungjin.wam.domain.member.service;

import com.chungjin.wam.domain.auth.service.RedisService;
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

import static com.chungjin.wam.global.exception.error.ErrorCodeType.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final QnaRepository qnaRepository;
    private final SupportRepository supportRepository;
    private final SupportLikeRepository supportLikeRepository;

    private final RedisService redisService;

    private final MemberMapper memberMapper;

    private final PasswordEncoder passwordEncoder;

    /**
     * 로그인 회원의 email로 회원 정보 조회
     */
    public MemberResponseDto getMemberProfile(Long memberId) {
        //memberId로 Member 객체 가져오기
        Member member = getMember(memberId);
        //Entity -> Dto
        return memberMapper.toDto(member);
    }

    /**
     * 회원 수정
     */
    @Transactional
    public void updateMember(Long memberId, UpdateMemberRequestDto updateMemberReq) {
        //memberId로 Member 객체 가져오기
        Member member = getMember(memberId);

        //이름, 휴대폰 번호 모두 입력하면 GUEST 인 사용자의 권한을 USER 로 변경
        if (updateMemberReq.getName() != null && updateMemberReq.getPhoneNumber() != null && member.getAuthority() == Authority.ROLE_GUEST) {
            member.updateAuthority(Authority.ROLE_USER);
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
        Member member = getMember(memberId);

        //입력한 비밀번호가 암호화된 비밀번호와 맞는지 확인
        if (!passwordEncoder.matches(updatePwReq.getNowPassword(), member.getPassword())) {
            log.info("비밀번호 일치 여부: {}", passwordEncoder.matches(updatePwReq.getNowPassword(), member.getPassword()));
            throw new CustomException(INVALID_PASSWORD);
        }

        //비밀번호 암호화 후 변경
        member.updatePw(passwordEncoder.encode(updatePwReq.getNewPassword()));
    }

    /**
     * 회원 탈퇴
     */
    @Transactional
    public void deleteMember(Long memberId, String password) {
        //memberId로 로그인 한 Member 객체 가져오기
        Member member = getMember(memberId);

        //입력한 비밀번호가 암호화된 비밀번호와 맞는지 확인
        if (!passwordEncoder.matches(password, member.getPassword())) {
            log.info("비밀번호 일치 여부: {}", passwordEncoder.matches(password, member.getPassword()));
            throw new CustomException(INVALID_PASSWORD);
        }

        //Redis 에서 RefreshToken 삭제
        redisService.deleteData(memberId.toString());

        //DB 에서 영구 삭제
        memberRepository.delete(member);
    }

    //memberId로 Member 객체 조회하는 함수
    private Member getMember (Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
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
        return PageResponse.builder()
                .content(qnaDtos)	//Qna 목록
                .pageNo(pageNo) //현재 페이지 번호
                .pageSize(qnaPage.getSize()) //페이지당 항목 수
                .totalElements(qnaPage.getTotalElements())   //전체 Qna 수
                .totalPages(qnaPage.getTotalPages()) //전체 페이지 수
                .last(qnaPage.isLast())  //마지막 페이지 여부
                .build();
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
        return PageResponse.builder()
                .content(supportDtos)	//Support 목록
                .pageNo(pageNo) //현재 페이지 번호
                .pageSize(supportPage.getSize()) //페이지당 항목 수
                .totalElements(supportPage.getTotalElements())   //전체 Support 수
                .totalPages(supportPage.getTotalPages()) //전체 페이지 수
                .last(supportPage.isLast())  //마지막 페이지 여부
                .build();
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
            likeDtos.add(convertToLikeDto(support)); //Support를 MyLikeDto로 변환하여 리스트에 추가
        }
        return PageResponse.builder()
                .content(likeDtos)	//Support 목록
                .pageNo(pageNo) //현재 페이지 번호
                .pageSize(likePage.getSize()) //페이지당 항목 수
                .totalElements(likePage.getTotalElements())   //전체 Support 수
                .totalPages(likePage.getTotalPages()) //전체 페이지 수
                .last(likePage.isLast())  //마지막 페이지 여부
                .build();
    }

    //Entity -> Dto (Like)
    public MyLikeResponseDto convertToLikeDto(Support support) {
        return MyLikeResponseDto.builder()
                .supportId(support.getSupportId())
                .title(support.getTitle())
                .nickname(support.getMember().getNickname())
                .createDate(support.getCreateDate())
                .supportStatus(support.getSupportStatus())
                .build();
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
        return PageResponse.builder()
                .content(memberDtos)	//Member 목록
                .pageNo(pageNo) //현재 페이지 번호
                .pageSize(memberPage.getSize()) //페이지당 항목 수
                .totalElements(memberPage.getTotalElements())   //전체 Member 수
                .totalPages(memberPage.getTotalPages()) //전체 페이지 수
                .last(memberPage.isLast())  //마지막 페이지 여부
                .build();
    }

}
