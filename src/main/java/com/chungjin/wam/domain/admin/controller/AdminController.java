package com.chungjin.wam.domain.admin.controller;

import com.chungjin.wam.domain.auth.service.CustomUserDetails;
import com.chungjin.wam.global.common.PageResponse;
import com.chungjin.wam.domain.member.service.MemberService;
import com.chungjin.wam.domain.qna.dto.response.QnaResponseDto;
import com.chungjin.wam.domain.qna.service.QnaService;
import com.chungjin.wam.domain.support.dto.response.SupportResponseDto;
import com.chungjin.wam.domain.support.service.SupportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
//@PreAuthorize("hasRole('ROLE_ADMIN')")  //관리자만 가능
public class AdminController {

    private final MemberService memberService;
    private final QnaService qnaService;
    private final SupportService supportService;

    /**
     * 모든 회원 조회
     */
    @GetMapping("/member/page={page}")
    public ResponseEntity<PageResponse> readAllMember(@PathVariable(value = "page") int pageNo) {
        return ResponseEntity.ok().body(memberService.readAllMember(pageNo));
    }

    /**
     * 회원 삭제 (관리자)
     */
    @DeleteMapping("/member/leave/{memberId}")
    public ResponseEntity<String> deleteMember(@AuthenticationPrincipal CustomUserDetails userDetails,
                                               @PathVariable(value = "memberId") Long selectedMemberId) {
        memberService.deleteMember(userDetails.getMember().getMemberId(), selectedMemberId);
        return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
    }

    /**
     * 모든 QnA 조회
     */
    @GetMapping("/qna/page={page}")
    public ResponseEntity<PageResponse> readAllQna(@PathVariable(value = "page") int pageNo) {
        return ResponseEntity.ok().body(qnaService.readAllQna(pageNo));
    }

    /**
     * QnA 삭제 (관리자)
     */
    @DeleteMapping("/qna/{qnaId}")
    public ResponseEntity<String> deleteQna(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @PathVariable(value = "qnaId") Long qnaId) {
        qnaService.deleteQna(userDetails.getMember().getMemberId(), qnaId);
        return ResponseEntity.ok("QnA 게시물이 삭제되었습니다.");
    }

    /**
     * 모든 후원 조회
     */
    @GetMapping("/support/page={page}")
    public ResponseEntity<PageResponse> readAllSupport(@PathVariable(value = "page") int pageNo) {
        return ResponseEntity.ok().body(supportService.readAllSupport(pageNo));
    }

    /**
     * 후원 삭제 (관리자)
     */
    @DeleteMapping("/support/{supportId}")
    public ResponseEntity<String> deleteSupport(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                @PathVariable(value = "supportId") Long supportId) {
        supportService.deleteSupport(userDetails.getMember().getMemberId(), supportId);
        return ResponseEntity.ok("후원이 삭제되었습니다.");
    }

}
