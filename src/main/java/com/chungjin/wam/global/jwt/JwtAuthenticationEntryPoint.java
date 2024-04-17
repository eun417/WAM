package com.chungjin.wam.global.jwt;

import com.chungjin.wam.global.exception.error.ErrorCodeType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * 유효한 자격 증명을 제공하지 않고 접근하려 할 때 401 에러 리턴
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.info("JwtAuthenticationEntryPoint 실행");
        String exception = (String)request.getAttribute("exception");
        log.info("JwtAuthenticationEntryPoint - exception: {}", exception);

        if (exception == null) {
            setErrorResponse(request, response, ErrorCodeType.UNAUTHORIZED);
        }
        else if (exception.equals(ErrorCodeType.WRONG_TYPE_SIGNATURE.getCode())) {
            setErrorResponse(request, response, ErrorCodeType.WRONG_TYPE_SIGNATURE);
        }
        else if (exception.equals(ErrorCodeType.ACCESS_TOKEN_EXPIRED.getCode())) {
            setErrorResponse(request, response, ErrorCodeType.ACCESS_TOKEN_EXPIRED);
        }
        else if (exception.equals(ErrorCodeType.WRONG_TYPE_TOKEN.getCode())) {
            setErrorResponse(request, response, ErrorCodeType.WRONG_TYPE_TOKEN);
        }
        else {
            setErrorResponse(request, response, ErrorCodeType.UNAUTHORIZED);
        }
    }

    private void setErrorResponse(HttpServletRequest request, HttpServletResponse response, ErrorCodeType errorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        final Map<String, Object> body = new HashMap<>();
        body.put("status", errorCode.getHttpStatus());
        body.put("message", errorCode.getMessage());
        body.put("path", request.getServletPath());
        body.put("timestamp", LocalDateTime.now().toString());
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }

}
