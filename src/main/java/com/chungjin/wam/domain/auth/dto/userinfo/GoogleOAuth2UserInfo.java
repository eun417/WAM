package com.chungjin.wam.domain.auth.dto.userinfo;

import java.util.Map;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    /**
     * 유저 정보가 감싸져 있지 않기 때문에
     * 바로 get으로 유저 정보 Key를 사용해서 꺼내면 됨
     */

    @Override
    public String getId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getNickname() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

}
