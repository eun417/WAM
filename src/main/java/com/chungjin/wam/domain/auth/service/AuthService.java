package com.chungjin.wam.domain.auth.service;

import com.chungjin.wam.domain.auth.dto.TokenDto;
import com.chungjin.wam.domain.auth.dto.request.*;
import com.chungjin.wam.domain.auth.dto.response.FindEmailResponseDto;
import com.chungjin.wam.domain.auth.entity.RefreshToken;
import com.chungjin.wam.domain.auth.repository.RefreshTokenRepository;
import com.chungjin.wam.domain.member.entity.Authority;
import com.chungjin.wam.domain.member.entity.Member;
import com.chungjin.wam.domain.member.repository.MemberRepository;
import com.chungjin.wam.global.jwt.JwtTokenProvider;
import com.chungjin.wam.global.util.DataMasking;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.UnsupportedEncodingException;
import java.time.Duration;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional//(readOnly = true)
public class AuthService {

    private final EmailService emailService;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;


    /**
     * 회원가입 - 인증코드 메일 발송
     */
    public void sendCodeToEmail(EmailRequestDto emailReq) throws MessagingException, UnsupportedEncodingException {
        //사용자가 입력한 이메일로 사용자가 있는지 확인
        Member member = memberRepository.findByEmail(emailReq.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."));

        //인증코드 전송
        emailService.sendCodeMail(emailReq.getEmail());
    }

    /**
     * 회원가입
     */
    public void signUp(SignUpRequestDto signUpReq) {
        //이미 가입되어 있는 사용자 확인
        if(memberRepository.existsByEmail(signUpReq.getEmail())) throw new ResponseStatusException(CONFLICT, "이미 가입되어 있는 회원입니다");

        //Dto -> Entity 변환 후 저장
        memberRepository.save(Member.builder()
                .email(signUpReq.getEmail())
                .password(passwordEncoder.encode(signUpReq.getPassword()))
                .name(signUpReq.getName())
                .phoneNumber(signUpReq.getPhoneNumber())
                .authority(Authority.ROLE_USER)
                .nickname(signUpReq.getNickname())
                .build());
    }

    /**
     * 로그인
     */
    public TokenDto login(LoginRequest loginReq) {
        //사용자가 입력한 이메일로 사용자가 있는지 확인
        Member member = memberRepository.findByEmail(loginReq.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."));

        //비밀번호 체크
        if (!passwordEncoder.matches(loginReq.getPassword(), member.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }

        //로그인 이메일, 비밀번호를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = loginReq.toAuthentication();

        //검증(사용자 비밀번호 체크)
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        //인증 정보를 기반으로 JWT 토큰 생성, 발급
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(authentication);

        //RefreshToken을 DB에 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .memberId(Long.parseLong(authentication.getName()))  //memberId
                .value(tokenDto.getRefreshToken())  //RefreshToken 값
                .build();

        refreshTokenRepository.save(refreshToken);

        //토큰 발급
        return tokenDto;
    }

    /**
     * Refresh
     */
    public TokenDto refresh(TokenRequestDto tokenReq) {
        //Refresh 토큰 검증
        if (!jwtTokenProvider.validateToken(tokenReq.getRefreshToken())) {
            throw new RuntimeException("Refresh Token이 유효하지 않습니다.");
        }

        //Access Token에서 email 가져오기
        Authentication authentication = jwtTokenProvider.getAuthentication(tokenReq.getAccessToken());

        //저장소에서 email을 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findById(Long.parseLong(authentication.getName()))
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        //Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenReq.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        //새로운 토큰 생성
        TokenDto newTokenDto = jwtTokenProvider.generateTokenDto(authentication);

        //저장소 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(newTokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        //토큰 발급
        return newTokenDto;
    }

    /**
     * 로그아웃
     */
    public void logout(Long memberId) {
        //memberId로 RefreshToken 객체 조회
        RefreshToken token = refreshTokenRepository.findByMemberId(memberId);

        //DB에서 해당 RefreshToken 삭제
        refreshTokenRepository.deleteById(token.getMemberId());
    }

    /**
     * 이메일 찾기
     */
    public FindEmailResponseDto findEmail(FindEmailRequestDto findEmailReq) {
        String phoneNumber = findEmailReq.getPhoneNumber();
        String name = findEmailReq.getName();

        Member member = memberRepository.findByNameAndPhoneNumber(name, phoneNumber);

        //이메일 마스킹
        String maskedEmail = DataMasking.emailMasking(member.getEmail());

        return FindEmailResponseDto.builder().findEmail(maskedEmail).build();
    }

}
