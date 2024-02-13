package com.chungjin.wam.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 토큰의 인증정보를 현재 실행 중인 SecurityContext에 저장
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //Request Header에서 토큰을 꺼냄
        String jwt = resolveToken(request);
        String requestURI = request.getRequestURI();

        //validateToken으로 토큰 유효성 검사
        if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
            //토큰 유효성 검증 통과(정상 토큰)
            Authentication authentication = jwtTokenProvider.getAuthentication(jwt);   //토큰에서 Authentication 객체 받아옴
            SecurityContextHolder.getContext().setAuthentication(authentication);   //SecurityContext에 저장(set)
            log.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);

        } else {
            //토큰 유효성 검증 통과 X
            log.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
        }

        filterChain.doFilter(request, response);

    }

    /**
     * 필터링하기 위해선 토큰 정보 필요
     * -> Request Header에서 토큰 정보를 꺼내옴
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }

        return null;
    }

}
