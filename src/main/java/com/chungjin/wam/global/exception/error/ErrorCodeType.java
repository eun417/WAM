package com.chungjin.wam.global.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCodeType implements ErrorCode {

    //Common
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "유효하지 않은 매개변수가 포함되어 있습니다."),    //클라이언트의 요청이 올바르지 않음
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 리소스를 찾을 수 없습니다."),

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생하였습니다."),
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "일시적으로 서비스를 사용할 수 없습니다."),

    //Member
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "회원을 찾을 수 없습니다."),
    EMAIL_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 이메일입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "이미 사용 중인 이메일입니다."),

    //Auth
    INCORRECT_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "잘못된 인증번호입니다."),
    ALREADY_LOGGED_OUT(HttpStatus.BAD_REQUEST, "이미 로그아웃된 회원입니다."),

    //Token
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    TOKEN_USER_MISMATCH(HttpStatus.BAD_REQUEST, "토큰의 유저 정보가 일치하지 않습니다."),

    //QnA
    QNA_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 QnA입니다."),

    //Support
    SUPPORT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 후원입니다."),
    SUPPORT_LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 좋아요입니다."),

    //Comment
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 댓글입니다."),

    //Image
    UPLOAD_FILE_FAILED(HttpStatus.BAD_REQUEST, "이미지 업로드에 실패했습니다."),
    INVALID_FILE_FORMAT(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일입니다."),
    DELETE_FILE_FAILED(HttpStatus.BAD_REQUEST, "이미지 삭제에 실패했습니다."),

    //결제
    PAYMENT_AMOUNT_MISMATCH(HttpStatus.BAD_REQUEST, "입력한 금액과 실제 결제한 금액이 다릅니다."),

    //메일
    SEND_MAIL_FAILED(HttpStatus.BAD_REQUEST, "메일 전송에 실패했습니다."),

    ;

    private final HttpStatus httpStatus;
    private final String message;

}