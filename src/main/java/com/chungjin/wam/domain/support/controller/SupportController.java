package com.chungjin.wam.domain.support.controller;

import com.chungjin.wam.domain.support.dto.SupportDto;
import com.chungjin.wam.domain.support.dto.request.SupportRequestDto;
import com.chungjin.wam.domain.support.dto.request.UpdateSupportRequestDto;
import com.chungjin.wam.domain.support.dto.response.SupportDetailDto;
import com.chungjin.wam.domain.support.service.SupportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/support")
public class SupportController {

    private final SupportService supportService;

    /**
     * 후원 생성
     */
    @PostMapping("/")
    public ResponseEntity<String> createSupport(@AuthenticationPrincipal User user,
                                                @RequestBody @Valid SupportRequestDto supportReq) {
        supportService.createSupport(user.getUsername(), supportReq);
        return ResponseEntity.ok("success");
    }

    /**
     * 후원 조회
     */
    @GetMapping("/{supportId}")
    public ResponseEntity<SupportDetailDto> readSupport(@PathVariable(value = "supportId") Long supportId) {
        return ResponseEntity.ok().body(supportService.readSupport(supportId));
    }

    /**
     * 후원 List 조회 (Pagination)
     */
    @GetMapping("/page={page}")
    public ResponseEntity<List<SupportDto>> readAllSupport(@PathVariable(value = "page") int page) {
        return ResponseEntity.ok().body(supportService.readAllSupport(page));
    }

    /**
     * 후원 수정
     */
    @PutMapping("/{supportId}")
    public ResponseEntity<String> updateSupport(@AuthenticationPrincipal User user,
                                                @PathVariable(value = "supportId") Long supportId,
                                                @RequestBody @Valid UpdateSupportRequestDto updateSupportReq) {
        System.out.println(user.getUsername());
        supportService.updateSupport(user.getUsername(), supportId, updateSupportReq);
        return ResponseEntity.ok("success");
    }

    /**
     * 후원 삭제
     */
    @DeleteMapping("/{supportId}")
    public ResponseEntity<String> deleteSupport(@AuthenticationPrincipal User user,
                                                @PathVariable(value = "supportId") Long supportId) {
        supportService.deleteSupport(user.getUsername(), supportId);
        return ResponseEntity.ok("success");
    }

}
