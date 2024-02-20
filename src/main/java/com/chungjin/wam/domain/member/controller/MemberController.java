package com.chungjin.wam.domain.member.controller;

import com.chungjin.wam.domain.auth.service.CustomUserDetails;
import com.chungjin.wam.domain.member.dto.request.UpdateMemberRequestDto;
import com.chungjin.wam.domain.member.dto.response.MemberDto;
import com.chungjin.wam.domain.member.dto.response.MyQnaResponseDto;
import com.chungjin.wam.domain.member.dto.response.MySupportResponseDto;
import com.chungjin.wam.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    /**
     * 로그인 회원의 email로 회원 정보 조회
     */
    @GetMapping("/mypage/profile")
    public ResponseEntity<MemberDto> getMemberProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok().body(memberService.getMemberProfile(userDetails.getMember().getMemberId()));
    }

    /**
     * 회원 수정
     */
    @PutMapping("/mypage/profile")
    public ResponseEntity<String> updateMember(@AuthenticationPrincipal CustomUserDetails userDetails,
                                               @RequestBody @Valid UpdateMemberRequestDto updateMembmerDto) {
        memberService.updateMember(userDetails.getMember().getMemberId(), updateMembmerDto);
        return ResponseEntity.ok("success");
    }

    /**
     * 회원 탈퇴
     */
    @DeleteMapping("/mypage/leave")
    public ResponseEntity<String> deleteUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        memberService.deleteMember(userDetails.getMember().getMemberId());
        return ResponseEntity.ok("success");
    }

    /**
     * 자신이 작성한 QnA List 조회 (Pagination)
     */
    @GetMapping("/mypage/qna/{page}")
    public ResponseEntity<List<MyQnaResponseDto>> getMyQna(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                           @PathVariable(value = "page") int page) {
        return ResponseEntity.ok().body(memberService.getMyQna(userDetails.getMember().getMemberId(), page));
    }

    /**
     * 자신이 작성한 후원 List 조회 (Pagination)
     */
    @GetMapping("/mypage/support/{page}")
    public ResponseEntity<List<MySupportResponseDto>> getMySupport(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                   @PathVariable(value = "page") int page) {
        return ResponseEntity.ok().body(memberService.getMySupport(userDetails.getMember().getMemberId(), page));
    }

    /**
     * 자신이 추가한 좋아요 조회 (Pagination)
     */
    @GetMapping("/mypage/like/{page}")
    public ResponseEntity<List<MySupportResponseDto>> getMyLike(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                @PathVariable(value = "page") int page) {
        return ResponseEntity.ok().body(memberService.getMyLike(userDetails.getMember().getMemberId(), page));
    }

}
