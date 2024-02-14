package com.chungjin.wam.domain.member.controller;

import com.chungjin.wam.domain.member.dto.MemberDto;
import com.chungjin.wam.domain.member.entity.Member;
import com.chungjin.wam.domain.member.service.MemberService;
import com.chungjin.wam.domain.qna.entity.Qna;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    /**
     * Id로 회원 조회
     * */
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberDto> getMemberById(@PathVariable(value = "memberId") Long memberId) {
        return ResponseEntity.ok().body(memberService.getMemberById(memberId));
    }

    /**
     * 회원 수정
     * */
    @PutMapping("/")
    public ResponseEntity<String> updateMember(@RequestBody MemberDto updateMembmerDto) {
        memberService.updateMember(updateMembmerDto);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    /**
     * 회원 삭제
     * */
    @DeleteMapping("/{memberId}")
    public ResponseEntity<String> deleteUser(@PathVariable(value = "memberId") Long memberId) {
        memberService.deleteMember(memberId);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

}
