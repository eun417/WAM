package com.chungjin.wam.global.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.FieldError;

import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class ErrorResponse {

    private final String code;  //에러 코드
    private final String message;   //에러 메시지

    //에러가 없다면 응답이 내려가지 않게 처리
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<ValidationError> errors; //예외에 대한 유효성 검사 오류 목록 리스트

    /**
     * @Valid로 에러가 들어왔을 때(유효성 검사 오류),
     * 어느 필드에서 에러가 발생했는 지에 대한 응답 처리
     */
    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class ValidationError {
        private final String field; //에러가 발생한 필드의 이름
        private final String message;

        //FieldError로부터 ValidationError 객체를 생성하여 반환
        public static ValidationError of(final FieldError fieldError) {
            return ValidationError.builder()
                    .field(fieldError.getField())
                    .message(fieldError.getDefaultMessage())
                    .build();
        }
    }

}
