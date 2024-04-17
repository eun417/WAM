package com.chungjin.wam.global.exception.error;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

    String name();
    HttpStatus getHttpStatus();
    String getCode();
    String getMessage();

}
