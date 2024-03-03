package com.chungjin.wam.domain.auth.dto;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

    private Long memberId;
    private String oauthId;

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes,
                            String nameAttributeKey, Long memberId, String oauthId) {
        super(authorities, attributes, nameAttributeKey);
        this.memberId = memberId;
        this.oauthId = oauthId;
    }

}
