package com.chungjin.wam.domain.admin.controller;

import com.chungjin.wam.domain.admin.service.AdminService;
import com.chungjin.wam.global.common.PageResponse;
import com.chungjin.wam.domain.member.service.MemberService;
import com.chungjin.wam.domain.qna.service.QnaService;
import com.chungjin.wam.domain.support.service.SupportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminApiController {

    private final AdminService adminService;
    private final MemberService memberService;
    private final QnaService qnaService;
    private final SupportService supportService;

    /**
     * 모든 회원 조회
     */
    @GetMapping("/member")
    public ResponseEntity<PageResponse> readAllMember(@RequestParam(value = "pageNo") int pageNo) {
        return ResponseEntity.ok().body(memberService.readAllMember(pageNo));
    }

    /**
     * 회원 삭제 (관리자)
     */
    @DeleteMapping("/member/{memberId}")
    public ResponseEntity<String> deleteMember(@PathVariable(value = "memberId") Long memberId) {
        adminService.deleteMember(memberId);
        return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
    }

    /**
     * 모든 QnA 조회
     */
    @GetMapping("/qna")
    public ResponseEntity<PageResponse> readAllQna(@RequestParam(value = "pageNo") int pageNo) {
        return ResponseEntity.ok().body(qnaService.readAllQna(pageNo));
    }

    /**
     * QnA 삭제 (관리자)
     */
    @DeleteMapping("/qna/{qnaId}")
    public ResponseEntity<String> deleteQna(@PathVariable(value = "qnaId") Long qnaId) {
        adminService.deleteQna(qnaId);
        return ResponseEntity.ok("QnA 게시물이 삭제되었습니다.");
    }

    /**
     * 모든 후원 조회
     */
    @GetMapping("/support")
    public ResponseEntity<PageResponse> readAllSupport(@RequestParam(value = "pageNo") int pageNo) {
        return ResponseEntity.ok().body(supportService.readAllSupport(pageNo));
    }

    /**
     * 후원 삭제 (관리자)
     */
    @DeleteMapping("/support/{supportId}")
    public ResponseEntity<String> deleteSupport(@PathVariable(value = "supportId") Long supportId) {
        adminService.deleteSupport(supportId);
        return ResponseEntity.ok("후원이 삭제되었습니다.");
    }

}
