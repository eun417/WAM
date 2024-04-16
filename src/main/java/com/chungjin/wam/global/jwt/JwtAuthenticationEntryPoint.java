package com.chungjin.wam.global.jwt;

import com.chungjin.wam.global.exception.ErrorResponse;
import com.chungjin.wam.global.exception.error.ErrorCodeType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * 유효한 자격 증명을 제공하지 않고 접근하려 할 때 401 에러 리턴
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info("JwtAuthenticationEntryPoint 실행");

        handleAuthenticationException(response);
    }

    // 예외 처리를 별도의 메서드로 분리
    private void handleAuthenticationException(HttpServletResponse response) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCodeType.UNAUTHORIZED.getHttpStatus().toString(), ErrorCodeType.UNAUTHORIZED.getMessage());
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonErrorResponse = objectMapper.writeValueAsString(errorResponse);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE); //application/json
        response.getWriter().write(jsonErrorResponse);
    }

}
