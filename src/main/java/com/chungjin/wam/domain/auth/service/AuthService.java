package com.chungjin.wam.domain.auth.service;

import com.chungjin.wam.domain.auth.dto.TokenDto;
import com.chungjin.wam.domain.auth.dto.request.*;
import com.chungjin.wam.domain.auth.dto.response.FindEmailResponseDto;
import com.chungjin.wam.domain.auth.dto.response.TokenResponseDto;
import com.chungjin.wam.domain.member.entity.Member;
import com.chungjin.wam.domain.member.repository.MemberRepository;
import com.chungjin.wam.global.exception.CustomException;
import com.chungjin.wam.global.jwt.JwtTokenProvider;
import com.chungjin.wam.global.util.DataMasking;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.chungjin.wam.domain.member.entity.Authority.ROLE_USER;
import static com.chungjin.wam.domain.member.entity.LoginType.LOCAL;
import static com.chungjin.wam.global.exception.error.ErrorCodeType.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;

    private final EmailService emailService;
    private final RedisService redisService;

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    /**
     * 회원가입 - 1. 인증코드 메일 발송
     */
    public void sendCodeToEmail(EmailRequestDto emailReq) {
        //이미 가입되어 있는 사용자 확인
        checkEmailExists(emailReq.getEmail());

        //인증코드 전송
        emailService.sendCodeMail(emailReq.getEmail());
    }

    /**
     * 회원가입 - 2. 인증코드 검증
     */
    public void verifyCode(VerifyEmailRequestDto verifyEmailReq) {
        //Redis 에 저장된 인증코드 가져오기
        String redisAuthCode = redisService.getData(verifyEmailReq.getEmail());

        //Redis 에 저장된 인증코드가 존재하지 않거나, 입력받은 인증코드와 일치하지 않을 때 에러 발생
        if(!redisService.checkExistsValue(redisAuthCode) || !redisAuthCode.equals(verifyEmailReq.getAuthCode())) {
            throw new CustomException(INCORRECT_VERIFICATION_CODE);
        }

        //입력받은 인증코드와 Redis 에 저장된 인증코드가 같은 경우 Redis 에서 해당 인증코드 삭제
        redisService.deleteData(redisAuthCode);
    }

    /**
     * 회원가입 - 3. 정보 입력
     */
    @Transactional
    public void signUp(SignUpRequestDto signUpReq) {
        //Dto -> Entity 변환 후 저장
        memberRepository.save(Member.builder()
                .email(signUpReq.getEmail())
                .password(passwordEncoder.encode(signUpReq.getPassword()))
                .name(signUpReq.getName())
                .phoneNumber(signUpReq.getPhoneNumber())
                .authority(ROLE_USER)
                .nickname(signUpReq.getNickname())
                .loginType(LOCAL)
                .build());
    }

    //중복 이메일 확인하는 함수
    public void checkEmailExists(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new CustomException(DUPLICATE_EMAIL);
        }
    }

    /**
     * 로그인
     * @param loginReq
     * @return Access Token, 만료 시간, Refresh Token
     */
    public TokenDto login(LoginRequest loginReq) {
        //사용자가 입력한 이메일로 사용자가 있는지 확인
        Member member = memberRepository.findByEmail(loginReq.getEmail())
                .orElseThrow(() -> new CustomException(EMAIL_NOT_FOUND));

        //비밀번호 확인
        if (!passwordEncoder.matches(loginReq.getPassword(), member.getPassword())) {
            throw new CustomException(INVALID_PASSWORD);
        }

        //로그인 이메일, 비밀번호로 인증 토큰 생성
        UsernamePasswordAuthenticationToken authenticationToken = loginReq.toAuthentication();
        //인증 수행
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        //인증 정보를 기반으로 JWT 토큰 발급
        return jwtTokenProvider.generateTokenDto(authentication);
    }

    /**
     * Refresh
     * @param tokenReq
     * @return 재발급된 Access Token
     */
    public TokenResponseDto refresh(TokenRequestDto tokenReq) {
        //refresh token 검증
        jwtTokenProvider.validateRefreshToken(tokenReq.getRefreshToken());

        //refresh token 으로 memberId 가져오기
        String memberIdString = jwtTokenProvider.getMemberId(tokenReq.getRefreshToken());

        //Redis 에서 memberId를 기반으로 values 가져옴
        List<String> values = redisService.getListData(memberIdString); //0: refresh token, 1: authorities
        String refreshToken = values.get(0);
        log.info("Refresh Token in Redis: {}", refreshToken);

        //전달받은 refresh token 과 Redis 에 저장된 refresh token 일치하는지 확인
        if (!refreshToken.equals(tokenReq.getRefreshToken())) {
            throw new CustomException(TOKEN_USER_MISMATCH);
        }

        //새로운 access token 발급
        return TokenResponseDto.builder()
                .accessToken(jwtTokenProvider.generateAccessToken(memberIdString, values.get(1)))
                .build();
    }

    /**
     * 로그아웃
     */
    public void logout(String memberIdString) {
        if (redisService.existData(memberIdString)) {
            //Redis 에서 해당 refresh token 삭제
            redisService.deleteData(memberIdString);
        }
    }

    /**
     * 이메일 찾기
     */
    public FindEmailResponseDto findEmail(FindEmailRequestDto findEmailReq) {
        String name = findEmailReq.getName();
        String phoneNumber = findEmailReq.getPhoneNumber();

        //이름, 휴대폰 번호로 Member 객체 조회
        Member member = memberRepository.findByNameAndPhoneNumber(name, phoneNumber)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        //이메일 마스킹
        String maskedEmail = DataMasking.emailMasking(member.getEmail());

        return FindEmailResponseDto.builder().findEmail(maskedEmail).build();
    }

    /**
     * 비밀번호 재설정 - 1. 링크 메일 전송
     */
    public void sendLinkToEmail(ChangePwLinkRequestDto changePwReq) {
        //이름, 이메일로 사용자 확인
        if(!memberRepository.existsByNameAndEmail(changePwReq.getName(), changePwReq.getEmail()))
            throw new CustomException(MEMBER_NOT_FOUND);

        //링크 메일 전송
        emailService.sendLinkMail(changePwReq.getEmail());
    }

    /**
     * 비밀번호 재설정 - 2. 정보 입력
     */
    @Transactional
    public void changePw(ChangePwRequestDto changePwReq, String authCode) {
        //Redis 에 저장된 이메일 가져오기
        String email = redisService.getData(authCode);

        //인증코드에 해당하는 이메일이 없는 경우
        if (email == null) {
            throw new CustomException(INCORRECT_VERIFICATION_CODE);
        }

        //이메일로 사용자 조회
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        //비밀번호 암호화 후 변경
        member.updatePw(passwordEncoder.encode(changePwReq.getNewPassword()));

        //Redis 에 저장된 데이터 삭제
        redisService.deleteData(authCode);
    }

}
