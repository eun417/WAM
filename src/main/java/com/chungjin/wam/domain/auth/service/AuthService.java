package com.chungjin.wam.domain.auth.service;

import com.chungjin.wam.domain.auth.dto.TokenDto;
import com.chungjin.wam.domain.auth.dto.request.*;
import com.chungjin.wam.domain.auth.dto.response.FindEmailResponseDto;
import com.chungjin.wam.domain.auth.entity.RefreshToken;
import com.chungjin.wam.domain.auth.repository.RefreshTokenRepository;
import com.chungjin.wam.domain.member.entity.Authority;
import com.chungjin.wam.domain.member.entity.Member;
import com.chungjin.wam.domain.member.repository.MemberRepository;
import com.chungjin.wam.global.common.RedisService;
import com.chungjin.wam.global.exception.CustomException;
import com.chungjin.wam.global.exception.error.ErrorCodeType;
import com.chungjin.wam.global.jwt.JwtTokenProvider;
import com.chungjin.wam.global.util.DataMasking;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
@Transactional//(readOnly = true)
public class AuthService {

    private final EmailService emailService;
    private final RedisService redisService;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;


    /**
     * 회원가입 - 1. 인증코드 메일 발송
     */
    public void sendCodeToEmail(EmailRequestDto emailReq) throws MessagingException, UnsupportedEncodingException {
        //이미 가입되어 있는 사용자 확인
        checkEmailExists(emailReq.getEmail());

        //인증코드 전송
        emailService.sendCodeMail(emailReq.getEmail());
    }

    /**
     * 회원가입 - 2. 인증코드 검증
     */
    public void verifyCode(VerifyEmailRequestDto verifyEmailReq) {
        //Redis에 저장된 인증코드 가져오기
        String redisAuthCode = redisService.getData(verifyEmailReq.getEmail());

        //Redis에 저장된 인증코드가 존재하지 않거나, 입력받은 인증코드와 일치하지 않을 때 에러 발생
        if(!redisService.checkExistsValue(redisAuthCode) || !redisAuthCode.equals(verifyEmailReq.getAuthCode())) {
            throw new CustomException(ErrorCodeType.INCORRECT_VERIFICATION_CODE);
        }
    }

    /**
     * 회원가입 - 3. 정보 입력
     */
    public void signUp(SignUpRequestDto signUpReq) {
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
     * 중복 이메일 확인
     */
    public void checkEmailExists(String email) {
        if(memberRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCodeType.DUPLICATE_EMAIL);
        }
    }

    /**
     * 로그인
     */
    public TokenDto login(LoginRequest loginReq) {
        //사용자가 입력한 이메일로 사용자가 있는지 확인
        Member member = memberRepository.findByEmail(loginReq.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCodeType.EMAIL_NOT_FOUND));

        //비밀번호 확인
        if (!passwordEncoder.matches(loginReq.getPassword(), member.getPassword())) {
            throw new CustomException(ErrorCodeType.INVALID_PASSWORD);
        }

        //로그인 이메일, 비밀번호로 인증 토큰 생성
        UsernamePasswordAuthenticationToken authenticationToken = loginReq.toAuthentication();
        //인증 수행
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        //인증 정보를 기반으로 JWT 토큰 생성, 반환
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(authentication);

        //RefreshToken을 생성하고 DB에 저장
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
            throw new CustomException(ErrorCodeType.TOKEN_EXPIRED);
        }

        //Access Token에서 email 가져오기
        Authentication authentication = jwtTokenProvider.getAuthentication(tokenReq.getAccessToken());

        //저장소에서 email을 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findById(Long.parseLong(authentication.getName()))
                .orElseThrow(() -> new CustomException(ErrorCodeType.UNAUTHORIZED));

        //Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenReq.getRefreshToken())) {
            throw new CustomException(ErrorCodeType.TOKEN_USER_MISMATCH);
        }

        //새로운 토큰 생성
        TokenDto newTokenDto = jwtTokenProvider.generateTokenDto(authentication);

        //새로운 RefreshToken 저장
        RefreshToken newRefreshToken = refreshToken.updateValue(newTokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        //새로운 토큰 발급
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

        //이름, 휴대폰 번호로 Member 객체 조회
        Member member = memberRepository.findByNameAndPhoneNumber(name, phoneNumber);

        //이메일 마스킹
        String maskedEmail = DataMasking.emailMasking(member.getEmail());

        return FindEmailResponseDto.builder().findEmail(maskedEmail).build();
    }

    /**
     * 비밀번호 재설정 - 링크 메일 전송
     */
    public void sendLinkToEmail(ChangePwLinkRequestDto changePwReq) throws MessagingException, UnsupportedEncodingException {
        //이름, 이메일로 사용자 확인
        if(!memberRepository.existsByNameAndEmail(changePwReq.getName(), changePwReq.getEmail())) throw new CustomException(ErrorCodeType.MEMBER_NOT_FOUND);

        //링크 메일 전송
        emailService.sendLinkMail(changePwReq.getEmail());
    }

    /**
     * 비밀번호 재설정
     */
    public void changePw(ChangePwRequestDto changePwReq, String authCode) {
        //Redis에 저장된 인증코드 가져오기
        String redisAuthCode = redisService.getData(changePwReq.getEmail());

        //인증코드가 만료되었거나, 사용자가 입력한 인증코드와 Redis에서 가져온 인증코드가 일치하지 않는 경우
        if (redisAuthCode == null || !redisAuthCode.equals(authCode)) {
            throw new CustomException(ErrorCodeType.INCORRECT_VERIFICATION_CODE);
        }

        //사용자가 입력한 이메일로 사용자 가져오기
        Member member = memberRepository.findByEmail(changePwReq.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCodeType.MEMBER_NOT_FOUND));

        //비밀번호 암호화 후 변경
        member.updatePw(passwordEncoder.encode(changePwReq.getNewPassword()));
    }

}
