package com.chungjin.wam.domain.auth.dto.userinfo;

import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        //getId는 Long으로 반환되어 (String)으로 캐스팅될 수 없음 -> String.valueOf()로 캐스팅
        return String.valueOf(attributes.get("id"));
    }

    /**
     * 카카오는 유저 정보가 'kakao_account.profile'으로 2번 감싸져있는 구조 ('kakao_account' -> 'profile')
     * 따라서 get을 2번 사용하여 데이터를 꺼낸 후 사용하고 싶은 정보의 Key로 꺼내서 사용
     */
    @Override
    public String getNickname() {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        if (account == null || profile == null) {
            return null;
        }

        return (String) profile.get("nickname");
    }

    @Override
    public String getEmail() {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");

        if (account == null) {
            return null;
        }

        return (String) account.get("email");
    }

}
