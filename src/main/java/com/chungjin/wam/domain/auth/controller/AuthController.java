package com.chungjin.wam.domain.auth.controller;

import com.chungjin.wam.domain.auth.dto.TokenDto;
import com.chungjin.wam.domain.auth.dto.request.*;
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
     * 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody @Valid SignUpRequestDto signUpReq) {
        authService.signUp(signUpReq);
        return ResponseEntity.ok("success");
    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody @Valid LoginRequest loginReq) {
        TokenDto token = authService.login(loginReq);
        return ResponseEntity.ok(token);
    }

    /**
     * Refresh
     */
    @PostMapping("/refresh")
    public ResponseEntity<TokenDto> refresh(@RequestBody @Valid TokenRequestDto tokenReq) {
        return ResponseEntity.ok(authService.refresh(tokenReq));
    }

    /**
     * 이메일 찾기
     */
    @GetMapping("/find-account")
    public ResponseEntity<FindEmailResponseDto> findEmail(@RequestBody @Valid FindEmailRequestDto findEmailReq) {
        return ResponseEntity.ok().body(authService.findEmail(findEmailReq));
    }

}
