package com.chungjin.wam.domain.auth.dto;

import com.chungjin.wam.domain.auth.dto.userinfo.GoogleOAuth2UserInfo;
import com.chungjin.wam.domain.auth.dto.userinfo.KakaoOAuth2UserInfo;
import com.chungjin.wam.domain.auth.dto.userinfo.OAuth2UserInfo;
import com.chungjin.wam.domain.member.entity.Authority;
import com.chungjin.wam.domain.member.entity.Member;
import com.chungjin.wam.domain.member.entity.LoginType;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.userdetails.User;

import java.util.Map;
import java.util.UUID;

/**
 * 각 소셜에서 받아오는 데이터가 다르므로
 * 소셜별로 데이터를 받는 데이터를 분기 처리
 */
@Getter
@Builder
public class OAuthAttributes {

    private String nameAttributeKey;    //OAuth2 로그인 진행 시 키가 되는 필드 값, PK와 같은 의미
    private OAuth2UserInfo oauth2UserInfo;  //소셜 타입별 로그인 유저 정보(닉네임, 이메일, 프로필 사진 등)

    /**
     * loginType에 맞는 메소드 호출하여 OAuthAttributes 객체 반환
     * 파라미터 -> userNameAttributeName : OAuth2 로그인 시 키(PK)가 되는 값 / attributes : OAuth 서비스의 유저 정보들
     * 소셜별 of 메소드(ofGoogle, ofKaKao, ofNaver)들은 각각 소셜 로그인 API에서 제공하는
     * 회원의 식별값(id), attributes, nameAttributeKey를 저장 후 build
     */
    public static OAuthAttributes of(LoginType loginType, String userNameAttributeName, Map<String, Object> attributes) {

//        if (loginType == LoginType.NAVER) {
//            return ofNaver(userNameAttributeName, attributes);
//        }
        if (loginType == LoginType.KAKAO) {
            return ofKakao(userNameAttributeName, attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new KakaoOAuth2UserInfo(attributes))
                .build();
    }

    public static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new GoogleOAuth2UserInfo(attributes))
                .build();
    }

//    public static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
//        return OAuthAttributes.builder()
//                .nameAttributeKey(userNameAttributeName)
//                .oauth2UserInfo(new NaverOAuth2UserInfo(attributes))
//                .build();
//    }

    /**
     * of 메소드로 OAuthAttributes 객체가 생성되어, 유저 정보들이 담긴 OAuth2UserInfo가 소셜 타입별로 주입된 상태
     * OAuth2UserInfo에서 oauthId(식별값), nickname, email을 가져와서 build
     * authority는 GUEST로 설정
     */
    public Member toEntity(LoginType loginType, OAuth2UserInfo oauth2UserInfo) {
        return Member.builder()
                .loginType(loginType)
                .oauthId(oauth2UserInfo.getId())
                .email(oauth2UserInfo.getEmail())
                .nickname(oauth2UserInfo.getNickname())
                .authority(Authority.GUEST)
                .build();
    }

}
