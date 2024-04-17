package com.chungjin.wam.global.exception.handler;

import com.chungjin.wam.global.exception.error.ErrorCodeType;
import com.chungjin.wam.global.exception.CustomException;
import com.chungjin.wam.global.exception.error.ErrorCode;
import com.chungjin.wam.global.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * RuntimeException 처리
     * 비즈니스 로직에서 발생한 오류를 클라이언트에게 전달하기 위해 사용
     */
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<Object> handleCustomException(CustomException e) {
        //CustomException 발생 시 예외 코드에 따른 응답을 반환
        log.warn("handleRuntimeException", e);
        return handleExceptionInternal(e.getErrorCode());
    }

    /**
     * IllegalArgumentException 처리
     * 메서드에 전달된 인자의 값이 유효하지 않을 때 발생
     */
    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException e) {
        //IllegalArgumentException 발생 시 로그를 기록하고 예외 코드와 메시지를 반환
        log.warn("handleIllegalArgument", e);
        return handleExceptionInternal(ErrorCodeType.INVALID_PARAMETER, e.getMessage());
    }

    /**
     * @Valid 어노테이션으로 넘어오는 에러 처리
     * 데이터 바인딩 과정에서 발생
     * 클라이언트의 잘못된 요청이나 입력 데이터에 대한 오류
     */
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<Object> handleBindException(BindException e) {
        //@Valid 어노테이션으로 넘어오는 에러를 처리하고, 해당 에러에 대한 응답을 반환
        log.warn("handleBindException", e);
        return handleExceptionInternal(e, ErrorCodeType.INVALID_PARAMETER);
    }

    /**
     * 기타 에러 처리
     * 주로 프로그래밍 오류나 시스템 장애와 관련하여 발생
     * 클라이언트에게는 상세한 오류 메시지를 반환 X
     */
//    @ExceptionHandler({Exception.class})    //{} : 배열(여러 예외 타입 처리)
//    protected ResponseEntity<Object> handleAllException(Exception ex) {
//        //모든 예외를 처리하고, 내부 서버 오류에 대한 응답을 반환
//        log.warn("handleAllException", ex);
//        return handleExceptionInternal(ErrorCodeType.INTERNAL_SERVER_ERROR);
//    }


    /**
     * 에러 처리 내부 메소드둘
     */

    //CustomException, 기타 에러 처리 메세지를 보내는 메소드
    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode) {
        //예외 코드에 따른 응답을 생성하여 반환
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode));
    }

    //에러 처리 메세지를 만드는 메소드
    private ErrorResponse makeErrorResponse(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .build();
    }

    //IllegalArgumentException 에러 처리 메세지 보내는 메소드
    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode, String message) {
        //예외 코드와 메시지에 따른 응답을 생성하여 반환
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode, message));
    }

    //에러 처리 메세지를 만드는 메소드
    private ErrorResponse makeErrorResponse(ErrorCode errorCode, String message) {
        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(message)
                .build();
    }

    //@Valid 어노테이션으로 넘어오는 에러 처리 메세지를 보내는 메소드
    private ResponseEntity<Object> handleExceptionInternal(BindException e, ErrorCode errorCode) {
        //@Valid 어노테이션으로 넘어오는 에러를 처리하고, ErrorResponse를 생성하여 반환
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(e, errorCode));
    }

    //에러 처리 메세지를 만드는 메소드
    private ErrorResponse makeErrorResponse(BindException e, ErrorCode errorCode) {
        //ValidationError 리스트 생성
        List<ErrorResponse.ValidationError> validationErrorList = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(ErrorResponse.ValidationError::of)
                .collect(Collectors.toList());

        //ErrorResponse 객체를 생성하여 반환
        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .errors(validationErrorList)
                .build();
    }

}
