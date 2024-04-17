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
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 토큰의 인증정보를 현재 실행 중인 SecurityContext에 저장
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //Request Header 에서 토큰을 꺼냄
        String accessToken = jwtTokenProvider.resolveToken(request);
//        String requestURI = request.getRequestURI();

        //validateToken으로 토큰 유효성 검사
        if (StringUtils.hasText(accessToken) && jwtTokenProvider.validateToken(accessToken)) {
            //토큰 유효성 검증 통과(정상 토큰)
            Authentication authentication = jwtTokenProvider.getAuthentication(accessToken); // 토큰에서 Authentication 객체 받아옴
            SecurityContextHolder.getContext().setAuthentication(authentication); // SecurityContext에 저장(set)
            log.info("SecurityContextHolder에 Authentication 객체를 저장했습니다. 인증 완료 {}", authentication.getName());
            log.info("저장된 객체 권한은 {} 입니다", authentication.getAuthorities());
        }

        filterChain.doFilter(request, response);
    }

}
