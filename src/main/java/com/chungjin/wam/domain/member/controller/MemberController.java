package com.chungjin.wam.domain.member.controller;

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
    public ResponseEntity<MemberDto> getMemberProfile(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(memberService.getMemberProfile(user.getUsername()));
    }

    /**
     * 회원 수정
     */
    @PutMapping("/mypage/profile")
    public ResponseEntity<String> updateMember(@AuthenticationPrincipal User user,
                                               @RequestBody @Valid UpdateMemberRequestDto updateMembmerDto) {
        memberService.updateMember(user.getUsername(), updateMembmerDto);
        return ResponseEntity.ok("success");
    }

    /**
     * 회원 탈퇴
     */
    @DeleteMapping("/mypage/leave")
    public ResponseEntity<String> deleteUser(@AuthenticationPrincipal User user) {
        memberService.deleteMember(user.getUsername());
        return ResponseEntity.ok("success");
    }

    /**
     * 자신이 작성한 QnA List 조회 (Pagination)
     */
    @GetMapping("/mypage/qna/{page}")
    public ResponseEntity<List<MyQnaResponseDto>> getMyQna(@AuthenticationPrincipal User user,
                                                           @PathVariable(value = "page") int page) {
        return ResponseEntity.ok().body(memberService.getMyQna(user.getUsername(), page));
    }

    /**
     * 자신이 작성한 후원 List 조회 (Pagination)
     */
    @GetMapping("/mypage/support/{page}")
    public ResponseEntity<List<MySupportResponseDto>> getMySupport(@AuthenticationPrincipal User user,
                                                                   @PathVariable(value = "page") int page) {
        return ResponseEntity.ok().body(memberService.getMySupport(user.getUsername(), page));
    }

}
