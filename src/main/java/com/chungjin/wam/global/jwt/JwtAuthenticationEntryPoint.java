//package com.chungjin.wam.global.jwt;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
//
//import java.io.IOException;
//
//public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
//
//    /**
//     * 유효한 자격 증명을 제공하지 않고 접근하려 할 때 401 에러 리턴
//     */
//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
//    }
//
//}
