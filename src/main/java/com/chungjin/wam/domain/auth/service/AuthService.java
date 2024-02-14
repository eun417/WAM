package com.chungjin.wam.domain.auth.service;

import com.chungjin.wam.domain.auth.dto.RefreshTokenDto;
import com.chungjin.wam.domain.auth.dto.TokenDto;
import com.chungjin.wam.domain.auth.dto.request.FindEmailRequestDto;
import com.chungjin.wam.domain.auth.dto.request.LoginRequest;
import com.chungjin.wam.domain.auth.dto.response.FindEmailResponseDto;
import com.chungjin.wam.domain.auth.dto.response.TokenResponseDto;
import com.chungjin.wam.domain.member.entity.Member;
import com.chungjin.wam.domain.member.repository.MemberRepository;
import com.chungjin.wam.global.jwt.JwtTokenProvider;
import com.chungjin.wam.global.util.DataMasking;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Transactional//(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    /**
     * 로그인
     */
    public TokenDto login(LoginRequest loginReq) {
        //사용자가 입력한 이메일과 비밀번호가 맞는지 확인
        Member member = memberRepository.findByEmailAndPassword(loginReq.getEmail(), loginReq.getPassword()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"이메일과 비밀번호를 다시 확인해주세요."));

        //Login ID/PW를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = loginReq.toAuthentication();

        //실제로 검증(사용자 비밀번호 체크)이 이루어짐
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        //인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(authentication);
        System.out.println(tokenDto);

//        //RefreshToken 저장
//        RefreshTokenDto refreshToken = RefreshTokenDto.builder()
//                .key(authentication.getName())
//                .value(tokenDto.getRefreshToken())
//                .build();
//
//        refreshTokenRepository.save(refreshToken);

        //토큰 발급
        return tokenDto;
    }

    /**
     * 이메일 찾기
     * */
    public FindEmailResponseDto findEmail(FindEmailRequestDto findEmailReq) {
        String phoneNumber = findEmailReq.getPhoneNumber();
        String name = findEmailReq.getName();

        Member member = memberRepository.findByNameAndPhoneNumber(name, phoneNumber);

        //이메일 마스킹
        String maskedEmail = DataMasking.emailMasking(member.getEmail());

        return FindEmailResponseDto.builder().findEmail(maskedEmail).build();
    }

}
