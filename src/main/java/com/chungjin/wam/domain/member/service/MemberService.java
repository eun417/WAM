package com.chungjin.wam.domain.member.service;

import com.chungjin.wam.domain.member.dto.MemberMapper;
import com.chungjin.wam.domain.member.dto.request.UpdateMemberRequestDto;
import com.chungjin.wam.domain.member.dto.response.MemberDto;
import com.chungjin.wam.domain.member.dto.response.MyLikeResponseDto;
import com.chungjin.wam.domain.qna.dto.response.QnaResponseDto;
import com.chungjin.wam.domain.support.dto.response.SupportResponseDto;
import com.chungjin.wam.global.common.PageResponse;
import com.chungjin.wam.domain.member.dto.response.MyQnaResponseDto;
import com.chungjin.wam.domain.member.dto.response.MySupportResponseDto;
import com.chungjin.wam.domain.member.entity.Authority;
import com.chungjin.wam.domain.member.entity.Member;
import com.chungjin.wam.domain.member.repository.MemberRepository;
import com.chungjin.wam.domain.qna.dto.QnaMapper;
import com.chungjin.wam.domain.qna.entity.Qna;
import com.chungjin.wam.domain.qna.repository.QnaRepository;
import com.chungjin.wam.domain.support.dto.SupportMapper;
import com.chungjin.wam.domain.support.entity.Support;
import com.chungjin.wam.domain.support.repository.SupportLikeRepository;
import com.chungjin.wam.domain.support.repository.SupportRepository;
import com.chungjin.wam.global.exception.CustomException;
import com.chungjin.wam.global.exception.error.ErrorCodeType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
        //memberId로 Member 객체 가져오기
        Member member = getMember(memberId);
        //Entity -> Dto
        return memberMapper.toDto(member);
    }

    /**
     * 회원 수정
     */
    public void updateMember(Long memberId, UpdateMemberRequestDto updateMembmerDto) {
        //memberId로 Member 객체 가져오기
        Member member = getMember(memberId);

        //로그인한 사용자가 마이페이지의 회원이 아닌 경우 에러 발생
        if(!memberId.equals(member.getMemberId())) throw new CustomException(ErrorCodeType.FORBIDDEN);

        //이름, 휴대폰 번호 입력하면 GUEST인 사용자의 권한을 USER로 변경
        if (member.getAuthority() == Authority.GUEST) {
            member.updateAuthority(Authority.USER);
        }

        //MapStruct로 수정
        memberMapper.updateFromDto(updateMembmerDto, member);
    }

    /**
     * 회원 탈퇴
     */
    public void deleteMember(Long memberId, Long selectedMemberId) {
        //memberId로 로그인 한 Member 객체 가져오기
        Member member = getMember(memberId);

        //로그인한 사용자가 마이페이지 회원인 경우 또는 관리자인 경우 탈퇴 가능
        if(memberId.equals(selectedMemberId) || member.getAuthority().equals(Authority.ADMIN)) {
            //DB에서 영구 삭제
            memberRepository.deleteById(selectedMemberId);
        } else {
            //그 외 에러 발생
            throw new CustomException(ErrorCodeType.FORBIDDEN);
        }
    }

    //memberId로 Member 객체 조회
    private Member getMember (Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCodeType.MEMBER_NOT_FOUND));
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
            qnaDtos.add(convertToQnaDto(qna)); // Qna를 MyQnaDto로 변환하여 리스트에 추가
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

    //Entity -> Dto
    public MyQnaResponseDto convertToQnaDto(Qna qna) {
        return MyQnaResponseDto.builder()
                .qnaId(qna.getQnaId())
                .title(qna.getTitle())
                .createDate(qna.getCreateDate())
                .viewCount(qna.getViewCount())
                .qnaCheck(qna.getQnaCheck())
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
            supportDtos.add(convertToSupportDto(support)); // Support를 MySupportDto로 변환하여 리스트에 추가
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

    //Entity -> Dto
    public MySupportResponseDto convertToSupportDto(Support support) {
        return MySupportResponseDto.builder()
                .supportId(support.getSupportId())
                .title(support.getTitle())
                .goalAmount(support.getGoalAmount())
                .supportAmount(support.getSupportAmount())
                .createDate(support.getCreateDate())
                .supportStatus(support.getSupportStatus())
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
            likeDtos.add(convertToLikeDto(support)); // Support를 MyLikeDto로 변환하여 리스트에 추가
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

    //Entity -> Dto
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
        //Member를 페이지별 조회
        Page<Member> memberPage = memberRepository.findAll(pageable);
        //현재 페이지의 Member 목록
        List<Member> members = memberPage.getContent();
        //EntityList -> DtoList
        List<Object> memberDtos = new ArrayList<>();
        for (Member member : members) {
            memberDtos.add(memberMapper.toDto(member)); // Member를 MemberDto로 변환하여 리스트에 추가
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
