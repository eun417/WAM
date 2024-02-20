package com.chungjin.wam.domain.auth.controller;

import com.chungjin.wam.domain.auth.dto.TokenDto;
import com.chungjin.wam.domain.auth.dto.request.*;
import com.chungjin.wam.domain.auth.dto.response.FindEmailResponseDto;
import com.chungjin.wam.domain.auth.service.AuthService;
import com.chungjin.wam.domain.auth.service.CustomUserDetails;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * 회원가입 - 인증코드 메일 발송
     */
    @PostMapping("/signup/email/send")
    public ResponseEntity<String> sendMessage(@RequestBody @Valid EmailRequestDto emailReq) throws MessagingException {
        authService.sendCodeToEmail(emailReq);
        return ResponseEntity.ok("success");
    }

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
        return ResponseEntity.ok(authService.login(loginReq));
    }

    /**
     * Refresh
     */
    @PostMapping("/refresh")
    public ResponseEntity<TokenDto> refresh(@RequestBody @Valid TokenRequestDto tokenReq) {
        return ResponseEntity.ok(authService.refresh(tokenReq));
    }

    /**
     * 로그아웃
     */
    @GetMapping("/logout")
    public ResponseEntity<String> logout(@AuthenticationPrincipal CustomUserDetails userDetails) {
        authService.logout(userDetails.getMember().getMemberId());
        return ResponseEntity.ok("success");
    }

    /**
     * 이메일 찾기
     */
    @GetMapping("/find-account")
    public ResponseEntity<FindEmailResponseDto> findEmail(@RequestBody @Valid FindEmailRequestDto findEmailReq) {
        return ResponseEntity.ok().body(authService.findEmail(findEmailReq));
    }

}
