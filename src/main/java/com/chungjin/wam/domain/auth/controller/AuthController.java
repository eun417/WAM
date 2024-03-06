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

import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * 회원가입 - 인증코드 메일 발송
     */
    @PostMapping("/signup/email/send")
    public ResponseEntity<String> sendMail(@RequestBody @Valid EmailRequestDto emailReq) throws MessagingException, UnsupportedEncodingException {
        authService.sendCodeToEmail(emailReq);
        return ResponseEntity.ok("인증 메일이 발송되었습니다.");
    }

    /**
     * 회원가입 - 인증코드 검증
     */
    @PostMapping("/signup/email/verify")
    public ResponseEntity<String> verifyEmail(@RequestBody @Valid VerifyEmailRequestDto verifyEmailReq) {
        authService.verifyCode(verifyEmailReq);
        return ResponseEntity.ok("인증되었습니다.");
    }

    /**
     * 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@ModelAttribute @Valid SignUpRequestDto signUpReq) {
        authService.signUp(signUpReq);
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
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
        return ResponseEntity.ok("로그아웃");
    }

    /**
     * 이메일 찾기
     */
    @PostMapping("/find-account")
    public ResponseEntity<FindEmailResponseDto> findEmail(@RequestBody @Valid FindEmailRequestDto findEmailReq) {
        return ResponseEntity.ok().body(authService.findEmail(findEmailReq));
    }

    /**
     * 비밀번호 재설정 - 링크 메일 전송
     */
    @PostMapping("/change-pw/email/send")
    public ResponseEntity<String> changePw(@RequestBody @Valid ChangePwLinkRequestDto changePwReq) throws MessagingException, UnsupportedEncodingException {
        authService.sendLinkToEmail(changePwReq);
        return ResponseEntity.ok("비밀번호 재설정 링크 메일이 발송되었습니다.");
    }

    /**
     * 비밀번호 재설정
     */
    @PutMapping("change-pw/{authCode}")
    public ResponseEntity<String> changePw(@RequestBody @Valid ChangePwRequestDto changePwReq,
                                           @PathVariable(value = "authCode") String authCode) {
        authService.changePw(changePwReq, authCode);
        return ResponseEntity.ok("비밀번호가 재설정되었습니다.");
    }

}
