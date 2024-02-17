package com.chungjin.wam.domain.member.controller;

import com.chungjin.wam.domain.member.dto.request.UpdateMemberRequestDto;
import com.chungjin.wam.domain.member.dto.response.MemberDto;
import com.chungjin.wam.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

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
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

}
