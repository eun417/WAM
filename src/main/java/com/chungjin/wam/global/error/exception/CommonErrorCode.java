package com.chungjin.wam.global.error.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "Invalid parameter included"),    //클라이언트의 요청이 올바르지 않음
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Resource not exists"),    //요청한 리소스를 찾을 수 없음

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Unauthorized access"),   //요청한 작업에 대한 권한이 없음
    FORBIDDEN(HttpStatus.FORBIDDEN, "Access forbidden"),    //요청한 작업에 대한 접근이 금지

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),   //서버 내부에서 예기치 않은 오류 발생
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "Service temporarily unavailable"); //서비스가 일시적으로 사용할 수 없는 상태

    private final HttpStatus httpStatus;
    private final String message;

}