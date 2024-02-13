package com.chungjin.wam.domain.auth.controller;

import com.chungjin.wam.domain.auth.dto.request.FindEmailRequestDto;
import com.chungjin.wam.domain.auth.dto.response.FindEmailResponseDto;
import com.chungjin.wam.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * 이메일 찾기
     * */
    @GetMapping("/find-account")
    public ResponseEntity<FindEmailResponseDto> findEmail(@RequestBody @Valid FindEmailRequestDto findEmailReq) {
        return ResponseEntity.ok().body(authService.findEmail(findEmailReq));
    }

}
