package com.chungjin.wam.domain.auth.handler;

import com.chungjin.wam.domain.auth.dto.CustomOAuth2User;
import com.chungjin.wam.domain.auth.dto.TokenDto;
import com.chungjin.wam.global.exception.CustomException;
import com.chungjin.wam.global.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
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

    /**
     * OAuth2 로그인 성공 시 호출
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        log.info("OAuth2 Login 성공");

        //사용자 정보 추출
        CustomOAuth2User oAuth2User = getCustomOAuth2User(authentication);

        //사용자 정보가 null 인 경우 에러 발생
        if (oAuth2User == null) {
            throw new CustomException(RESOURCE_NOT_FOUND);
        }

        //인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(authentication);

        //생성한 토큰을 쿼리 매개변수로 메인 페이지 URL에 추가
        String targetUrl = UriComponentsBuilder.fromUriString("/")
                .queryParam("accessToken", tokenDto.getAccessToken())
                .queryParam("refreshToken", tokenDto.getRefreshToken())
                .build().toUriString();

        //메인 페이지로 리다이렉트
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    /**
     * Authentication 객체에서 사용자 정보 가져오는 함수
     * @param authentication
     * @return CustomOAuth2User 캐스팅한 principal OR null
     */
    private CustomOAuth2User getCustomOAuth2User(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        //principal 이 CustomOAuth2User 타입인지 확인
        if (principal instanceof CustomOAuth2User) {
            //CustomOAuth2User 타입으로 캐스팅하여 사용
            return (CustomOAuth2User) principal;
        }
        return null;
    }

}
