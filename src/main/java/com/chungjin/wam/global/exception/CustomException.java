package com.chungjin.wam.global.exception;

import com.chungjin.wam.global.exception.error.ErrorCodeType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException {

    private final ErrorCodeType errorCode;

}
