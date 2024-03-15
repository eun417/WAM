package com.chungjin.wam.domain.member.controller;

import com.chungjin.wam.domain.auth.dto.CustomUserDetails;
import com.chungjin.wam.domain.member.dto.request.UpdateMemberRequestDto;
import com.chungjin.wam.domain.member.dto.response.MemberDto;
import com.chungjin.wam.domain.member.dto.response.MyQnaResponseDto;
import com.chungjin.wam.domain.member.dto.response.MySupportResponseDto;
import com.chungjin.wam.domain.member.service.MemberService;
import com.chungjin.wam.global.common.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberApiController {

    private final MemberService memberService;

    /**
     * 로그인 회원의 email로 회원 정보 조회
     */
    @GetMapping("/profile-detail")
    public ResponseEntity<MemberDto> getMemberProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok().body(memberService.getMemberProfile(userDetails.getMember().getMemberId()));
    }

    /**
     * 회원 수정
     */
    @PutMapping("/profile-detail")
    public ResponseEntity<String> updateMember(@AuthenticationPrincipal CustomUserDetails userDetails,
                                               @RequestBody @Valid UpdateMemberRequestDto updateMembmerDto) {
        memberService.updateMember(userDetails.getMember().getMemberId(), updateMembmerDto);
        return ResponseEntity.ok("회원 정보가 수정되었습니다.");
    }

    /**
     * 회원 탈퇴
     */
    @DeleteMapping("/leave/{memberId}")
    public ResponseEntity<String> deleteUser(@AuthenticationPrincipal CustomUserDetails userDetails,
                                             @PathVariable(value = "memberId") Long selectedMemberId) {
        memberService.deleteMember(userDetails.getMember().getMemberId(), selectedMemberId);
        return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
    }

    /**
     * 자신이 작성한 QnA List 조회 (Pagination)
     */
    @GetMapping("/qna/{page}")
    public ResponseEntity<PageResponse> getMyQna(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                 @PathVariable(value = "page") int pageNo) {
        return ResponseEntity.ok().body(memberService.getMyQna(userDetails.getMember().getMemberId(), pageNo));
    }

    /**
     * 자신이 작성한 후원 List 조회 (Pagination)
     */
    @GetMapping("/support/{page}")
    public ResponseEntity<PageResponse> getMySupport(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                   @PathVariable(value = "page") int pageNo) {
        return ResponseEntity.ok().body(memberService.getMySupport(userDetails.getMember().getMemberId(), pageNo));
    }

    /**
     * 자신이 추가한 좋아요 조회 (Pagination)
     */
    @GetMapping("/like/{page}")
    public ResponseEntity<PageResponse> getMyLike(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                @PathVariable(value = "page") int pageNo) {
        return ResponseEntity.ok().body(memberService.getMyLike(userDetails.getMember().getMemberId(), pageNo));
    }

}
