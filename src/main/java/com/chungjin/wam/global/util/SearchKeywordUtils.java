package com.chungjin.wam.global.util;

import com.chungjin.wam.global.exception.CustomException;

import static com.chungjin.wam.global.exception.error.ErrorCodeType.CHARACTER_LIMIT;

public class SearchKeywordUtils {

    public static String convertToBooleanModeKeyword(String keyword) {
        if (keyword == null || keyword.length() < 2) {
            throw new CustomException(CHARACTER_LIMIT);
        }

        StringBuilder result = new StringBuilder();
        int length = keyword.length();

        for (int i = 0; i < length - 1; i += 2) {
            if (i > 0) {
                result.append(" ");
            }
            result.append("+").append(keyword, i, i + 2);
        }

        // 홀수 길이인 경우 마지막 한 글자를 추가
        if (length % 2 != 0) {
            result.append(" +").append(keyword, length - 2, length - 1).append(keyword.charAt(length - 1));
        }


        return result.toString();
    }

}
