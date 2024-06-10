package com.chungjin.wam.global.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCodeType implements ErrorCode {

    //Common
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "COMMON-000", "유효하지 않은 매개변수가 포함되어 있습니다."),    //클라이언트의 요청이 올바르지 않음
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON-001", "해당 리소스를 찾을 수 없습니다."),

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON-002", "Unauthorized"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON-003", "접근 권한이 없습니다."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON-004", "서버 내부 오류가 발생하였습니다."),
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "COMMON-005", "일시적으로 서비스를 사용할 수 없습니다."),

    CHARACTER_LIMIT(HttpStatus.BAD_REQUEST, "COMMON-006", "검색어는 2글자 이상 입력해주세요."),

    //Member
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER-001", "회원을 찾을 수 없습니다."),
    EMAIL_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER-002", "존재하지 않는 이메일입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "MEMBER-003", "비밀번호가 일치하지 않습니다."),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "MEMBER-004", "이미 사용 중인 이메일입니다."),

    //Auth
    INCORRECT_AUTH_CODE(HttpStatus.BAD_REQUEST, "AUTH-001", "잘못된 인증번호입니다."),
    AUTH_CODE_NOT_FOUND(HttpStatus.NOT_FOUND, "AUTH-002", "인증 시간이 만료되었습니다."),
    ALREADY_LOGGED_OUT(HttpStatus.BAD_REQUEST, "AUTH-003", "이미 로그아웃된 회원입니다."),

    //JWT Token
    WRONG_TYPE_SIGNATURE(HttpStatus.UNAUTHORIZED, "JWT-001", "잘못된 JWT 서명입니다."),
    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "JWT-002", "토큰이 만료되었습니다."),
    WRONG_TYPE_TOKEN(HttpStatus.UNAUTHORIZED, "JWT-003", "지원되지 않는 JWT 토큰입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "JWT-004", "JWT 토큰이 잘못되었습니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "JWT-005", "리프레시 토큰이 만료되었습니다."),
    TOKEN_USER_MISMATCH(HttpStatus.BAD_REQUEST, "JWT-006", "토큰의 유저 정보가 일치하지 않습니다."),

    //QnA
    QNA_NOT_FOUND(HttpStatus.NOT_FOUND, "QNA-000", "존재하지 않는 QnA입니다."),

    //Support
    SUPPORT_NOT_FOUND(HttpStatus.NOT_FOUND, "SUPPORT-000", "존재하지 않는 후원입니다."),
    SUPPORT_LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "SUPPORT-001", "존재하지 않는 좋아요입니다."),

    //Comment
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT-000", "존재하지 않는 댓글입니다."),

    //Image
    UPLOAD_FILE_FAILED(HttpStatus.BAD_REQUEST, "IMAGE-000", "이미지 업로드에 실패했습니다."),
    INVALID_FILE_FORMAT(HttpStatus.BAD_REQUEST, "IMAGE-001", "잘못된 형식의 파일입니다."),
    DELETE_FILE_FAILED(HttpStatus.BAD_REQUEST, "IMAGE-002", "이미지 삭제에 실패했습니다."),

    //Payment
    PAYMENT_AMOUNT_MISMATCH(HttpStatus.BAD_REQUEST, "PAYMENT-000", "입력한 금액과 실제 결제한 금액이 다릅니다."),

    //Mail
    SEND_MAIL_FAILED(HttpStatus.BAD_REQUEST, "MAIL-000", "메일 전송에 실패했습니다."),

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}