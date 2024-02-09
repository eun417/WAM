package com.chungjin.wam.global.util;

public final class RegExp {

    /**
     * 비밀번호
     * 영문, 숫자, 특수문자 조합으로 이루어진 8~15자의 문자열
     */
    public static final String PASSWORD_REGEXP = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$";
    /**
     * 휴대폰 번호
     * "-" 없음
     * */
    public static final String PHONE_NUMBER_REGEXP = "^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$";

}
