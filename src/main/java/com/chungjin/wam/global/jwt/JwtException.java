package com.chungjin.wam.global.jwt;

import com.chungjin.wam.global.exception.CustomException;
import com.chungjin.wam.global.exception.error.ErrorCode;
import lombok.Getter;

@Getter
public class JwtException extends CustomException {
    public JwtException(ErrorCode errorCode) {
        super(errorCode);
    }
}
