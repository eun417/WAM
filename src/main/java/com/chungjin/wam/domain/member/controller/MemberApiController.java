package com.chungjin.wam.domain.member.controller;

import com.chungjin.wam.domain.auth.dto.CustomUserDetails;
import com.chungjin.wam.domain.member.dto.request.UpdateMemberRequestDto;
import com.chungjin.wam.domain.member.dto.request.UpdatePwRequestDto;
import com.chungjin.wam.domain.member.dto.response.MemberResponseDto;
import com.chungjin.wam.domain.member.service.MemberService;
import com.chungjin.wam.global.common.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberApiController {

    private final MemberService memberService;

    /**
     * 로그인 회원의 email로 회원 정보 조회
     */
    @GetMapping("/profile-detail")
    public ResponseEntity<MemberResponseDto> getMemberProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok().body(memberService.getMemberProfile(userDetails.getMember().getMemberId()));
    }

    /**
     * 회원 수정
     */
    @PutMapping("/profile-detail")
    public ResponseEntity<String> updateMember(@AuthenticationPrincipal CustomUserDetails userDetails,
                                               @RequestBody @Valid UpdateMemberRequestDto updateMemberReq) {
        memberService.updateMember(userDetails.getMember().getMemberId(), updateMemberReq);
        return ResponseEntity.ok("회원 정보가 수정되었습니다.");
    }

    /**
     * 비밀번호 변경
     */
    @PutMapping("/profile-detail/pw")
    public ResponseEntity<String> updatePw(@AuthenticationPrincipal CustomUserDetails userDetails,
                                           @RequestBody @Valid UpdatePwRequestDto updatePwReq) {
        memberService.updatePw(userDetails.getMember().getMemberId(), updatePwReq);
        return ResponseEntity.ok("비밀번호가 변경되었습니다.");
    }

    /**
     * 회원 탈퇴
     */
    @DeleteMapping("/leave")
    public ResponseEntity<String> deleteMember(@AuthenticationPrincipal CustomUserDetails userDetails,
                                               @RequestBody Map<String, String> password) {
        memberService.deleteMember(userDetails.getMember().getMemberId(), password.get("password"));
        return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
    }

    /**
     * 자신이 작성한 QnA List 조회 (Pagination)
     */
    @GetMapping("/qna-detail")
    public ResponseEntity<PageResponse> getMyQna(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                 @RequestParam("pageNo") int pageNo) {
        return ResponseEntity.ok().body(memberService.getMyQna(userDetails.getMember().getMemberId(), pageNo));
    }

    /**
     * 자신이 작성한 후원 List 조회 (Pagination)
     */
    @GetMapping("/support-detail")
    public ResponseEntity<PageResponse> getMySupport(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                     @RequestParam("pageNo") int pageNo) {
        return ResponseEntity.ok().body(memberService.getMySupport(userDetails.getMember().getMemberId(), pageNo));
    }

    /**
     * 자신이 추가한 좋아요 조회 (Pagination)
     */
    @GetMapping("/like-detail")
    public ResponseEntity<PageResponse> getMyLike(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                  @RequestParam("pageNo") int pageNo) {
        return ResponseEntity.ok().body(memberService.getMyLike(userDetails.getMember().getMemberId(), pageNo));
    }

}
