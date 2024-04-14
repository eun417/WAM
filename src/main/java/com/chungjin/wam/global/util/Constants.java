package com.chungjin.wam.global.util;

public final class Constants {

    /**
     * 정규식
     */
    //비밀번호: 영문, 숫자, 특수문자 조합으로 이루어진 8~15자의 문자열
    public static final String PASSWORD_REGEXP = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$";

    //휴대폰 번호: 숫자만 입력
    public static final String PHONE_NUMBER_REGEXP = "^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$";

    /**
     * S3 디렉토리
     */
    public static final String S3_SUPPORT = "support";
    public static final String S3_SUPPORT_FIRST = "support/firstImg";
    public static final String S3_QNA = "qna";

    /**
     * 자연환경조사 데이터 조회에 필요한 상수
     */
    public static final String BASE_URL = "https://www.nie-ecobank.kr/ecoapi/EcpeInfoService/wfs";
    public static final String SRC = "EPSG:5186";
    public static final String BBOX = "-410465.4539024363%2C23210.844119483023%2C811157.9208071957%2C634022.531474299";

    //양서파충류
    public static final String AMNRP_URL_DETAIL = "/getAmnrpPointWFS";
    public static final String AMNRP_TYPE_NAME = "mv_map_ecpe_amnrp_point";

    //포유류
    public static final String MML_URL_DETAIL = "/getMmlPointWFS";
    public static final String MML_TYPE_NAME = "mv_map_ecpe_mml_point";

    //조류
    public static final String BIRDS_URL_DETAIL = "/getBirdsPointWFS";
    public static final String BIRDS_TYPE_NAME = "mv_map_ecpe_birds_point";

    //어류
    public static final String FISHES_URL_DETAIL = "/getFishesPointWFS";
    public static final String FISHES_TYPE_NAME = "mv_map_ecpe_fishes_point";

}
