package com.chungjin.wam.domain.auth.service;

import com.chungjin.wam.domain.auth.dto.RefreshTokenDto;
import com.chungjin.wam.domain.auth.dto.TokenDto;
import com.chungjin.wam.domain.auth.dto.request.FindEmailRequestDto;
import com.chungjin.wam.domain.auth.dto.request.LoginRequest;
import com.chungjin.wam.domain.auth.dto.request.SignUpRequestDto;
import com.chungjin.wam.domain.auth.dto.response.FindEmailResponseDto;
import com.chungjin.wam.domain.auth.dto.response.TokenResponseDto;
import com.chungjin.wam.domain.member.dto.MemberDto;
import com.chungjin.wam.domain.member.dto.MemberMapper;
import com.chungjin.wam.domain.member.entity.Authority;
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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional//(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

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
//                .nickname(signUpReq.getNickname())
                .build());
    }

    /**
     * 로그인
     */
    public TokenDto login(LoginRequest loginReq) {
        //사용자가 입력한 이메일로 사용자가 있는지 확인
        if(!memberRepository.existsByEmail(loginReq.getEmail())) throw new ResponseStatusException(NOT_FOUND, "존재하지 않는 회원입니다.");

        //Login Email/PW를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = loginReq.toAuthentication();

        //검증(사용자 비밀번호 체크)
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        //인증 정보를 기반으로 JWT 토큰 생성, 발급
        return jwtTokenProvider.generateTokenDto(authentication);
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
