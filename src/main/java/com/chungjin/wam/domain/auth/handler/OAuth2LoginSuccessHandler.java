package com.chungjin.wam.domain.auth.handler;

import com.chungjin.wam.domain.auth.dto.TokenDto;
import com.chungjin.wam.domain.auth.entity.RefreshToken;
import com.chungjin.wam.domain.auth.repository.RefreshTokenRepository;
import com.chungjin.wam.domain.member.entity.Member;
import com.chungjin.wam.domain.member.repository.MemberRepository;
import com.chungjin.wam.global.jwt.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 Login 성공");

        //인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(authentication);

        //memberId 가져오기
        Member member = memberRepository.findByOauthId(authentication.getName());
        if (member != null) {
            Long memberId = member.getMemberId();

            //RefreshToken 객체 생성하고 DB에 저장
            RefreshToken rToken = RefreshToken.builder()
                    .memberId(memberId)  //memberId
                    .value(tokenDto.getRefreshToken())  //RefreshToken 값
                    .build();
            refreshTokenRepository.save(rToken);

            //TokenDto 객체를 JSON으로 변환하여 응답으로 전송
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            objectMapper.writeValue(response.getWriter(), tokenDto);

//        response.sendRedirect("/"); //메인 페이지로 리다이렉트
        }
    }

}
