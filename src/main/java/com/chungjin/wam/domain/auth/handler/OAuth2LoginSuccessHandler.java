package com.chungjin.wam.domain.auth.handler;

import com.chungjin.wam.domain.auth.dto.CustomOAuth2User;
import com.chungjin.wam.domain.auth.dto.TokenDto;
import com.chungjin.wam.global.exception.CustomException;
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
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

import static com.chungjin.wam.global.exception.error.ErrorCodeType.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 Login 성공");

        CustomOAuth2User oAuth2User = getCustomOAuth2User(authentication);


        if (oAuth2User == null) {
            throw new CustomException(RESOURCE_NOT_FOUND);
        }

        //인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(authentication);

        String targetUrl = UriComponentsBuilder.fromUriString("/")
                .queryParam("accessToken", tokenDto.getAccessToken())
                .queryParam("refreshToken", tokenDto.getRefreshToken())
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);

//        //헤더에 등록해서 전송
//        jwtTokenProvider.sendAccessAndRefreshToken(response, tokenDto.getAccessToken(), tokenDto.getRefreshToken());
//
//        response.sendRedirect("/"); //메인 페이지로 리다이렉트
    }

    /**
     * authentication.getPrincipal() 반환 객체가 CustomOAuth2User 타입인지 확인
     */
    private CustomOAuth2User getCustomOAuth2User(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        //principal이 CustomOAuth2User 타입인지 확인
        if (principal instanceof CustomOAuth2User) {
            //CustomOAuth2User 타입으로 캐스팅하여 사용
            return (CustomOAuth2User) principal;
        }
        return null;
    }

}
