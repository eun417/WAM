package com.chungjin.wam.global.util;

public class DataMasking {

    /**
     * 이메일 마스킹(앞 3자리 이후 '@' 전까지 마스킹)
     * */
    public static String emailMasking(String email) {
        String maskingResult = "";

        if (email.length() >= 3) {
            maskingResult = email.replaceAll("(?<=.{3}).(?=.*@)", "*");
        } else {
            maskingResult = email;
        }

        return maskingResult;
    }

}
