package com.chungjin.wam.domain.auth.service;

import com.chungjin.wam.domain.auth.dto.OAuthAttributes;
import com.chungjin.wam.domain.member.entity.LoginType;
import com.chungjin.wam.domain.member.entity.Member;
import com.chungjin.wam.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    private static final String NAVER = "naver";
    private static final String KAKAO = "kakao";

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("CustomOAuth2UserService.loadUser() 실행 - OAuth2 로그인 요청 진입");

        //OAuth 서비스(google, kakao, naver)에서 가져온 유저 정보를 담고있음
        OAuth2User oAuth2User = super.loadUser(userRequest);

        /**
         * userRequest에서 registrationId 추출 후 registrationId으로 LoginType 저장
         * http://localhost:8080/oauth2/authorization/kakao에서 kakao가 registrationId
         * userNameAttributeName은 이후에 nameAttributeKey로 설정된다.
         */
        //OAuth 서비스 이름(ex. google, kakao, naver)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        LoginType loginType = getLoginType(registrationId);

        //OAuth 로그인 시 키 값(PK)
        //구글, 카카오, 네이버 등 각각 다르기 때문에 변수로 받아서 넣음
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        //소셜 로그인에서 API가 제공하는 userInfo의 Json 값(OAuth2 서비스의 유저 정보)
        Map<String, Object> attributes = oAuth2User.getAttributes();

        //socialType에 따라 유저 정보를 통해 OAuthAttributes 객체 생성
        OAuthAttributes extractAttributes = OAuthAttributes.of(loginType, userNameAttributeName, attributes);

        Member createdMember = getMember(extractAttributes, loginType);

        //DefaultOAuth2User 객체 생성 후 반환
        return new DefaultOAuth2User(
                Collections.singletonList(new SimpleGrantedAuthority(createdMember.getAuthority().getKey())),
                attributes,
                extractAttributes.getNameAttributeKey()
        );
    }

    /**
     * registrationId("naver", "kakao", "google")로 분기 처리하여 맞는 소셜 타입 반환하는 메소드
     */
    private LoginType getLoginType(String registrationId) {
        if(NAVER.equals(registrationId)) {
            return LoginType.NAVER;
        }
        if(KAKAO.equals(registrationId)) {
            return LoginType.KAKAO;
        }
        return LoginType.GOOGLE;
    }

    /**
     * LoginType과 attributes에 들어있는 소셜 로그인의 식별값 id를 통해 회원을 찾아 반환하는 메소드
     * 만약 찾은 회원이 있다면 그대로 반환, 없다면 saveMember()를 호출하여 회원을 저장
     */
    private Member getMember(OAuthAttributes attributes, LoginType loginType) {
        Member findMember = memberRepository.findByLoginTypeAndOauthId(loginType,
                attributes.getOauth2UserInfo().getId()).orElse(null);

        if(findMember == null) {
            return saveMember(attributes, loginType);
        }
        return findMember;
    }

    /**
     * OAuthAttributes의 toEntity() 메소드를 통해 빌더로 User 객체 생성 후 반환
     * 생성된 User 객체를 DB에 저장 : socialType, socialId, email, role 값만 있는 상태
     */
    private Member saveMember(OAuthAttributes attributes, LoginType loginType) {
        Member createdMember = attributes.toEntity(loginType, attributes.getOauth2UserInfo());
        return memberRepository.save(createdMember);
    }

}
