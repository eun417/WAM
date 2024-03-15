package com.chungjin.wam.global.exception;

import com.chungjin.wam.global.exception.error.ErrorCode;
import com.chungjin.wam.global.exception.error.ErrorCodeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());   //CustomException이 발생할 때 ErrorCode에 정의된 메시지로 설정
        this.errorCode = errorCode;
    }

}
