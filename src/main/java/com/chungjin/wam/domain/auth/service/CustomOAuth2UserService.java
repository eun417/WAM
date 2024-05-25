package com.chungjin.wam.domain.auth.service;

import com.chungjin.wam.domain.auth.dto.CustomOAuth2User;
import com.chungjin.wam.domain.auth.dto.OAuthAttributes;
import com.chungjin.wam.domain.member.entity.LoginType;
import com.chungjin.wam.domain.member.entity.Member;
import com.chungjin.wam.domain.member.repository.MemberRepository;
import com.chungjin.wam.global.util.EntityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final EntityUtils entityUtils;

    private static final String NAVER = "naver";
    private static final String KAKAO = "kakao";

    /**
     * OAuth2 인증을 통해 사용자 정보 조회
     * @param userRequest
     * @return CustomOAuth2User 객체
     * @throws OAuth2AuthenticationException
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("CustomOAuth2UserService.loadUser() 실행 - OAuth2 로그인 요청 진입");

        //OAuth 서비스(google, kakao, naver)에서 가져온 유저 정보를 담고있음
        OAuth2User oAuth2User = super.loadUser(userRequest);

        /*
          userRequest 에서 registrationId 추출 후 registrationId 으로 가져온 LoginType 저장
          http://.../oauth2/authorization/kakao에서 kakao가 registrationId,
          userNameAttributeName 은 이후에 nameAttributeKey 로 설정됨
         */
        //OAuth 서비스 이름(ex: google, kakao, naver)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        LoginType loginType = getLoginType(registrationId);

        //OAuth 로그인 시 키 값(PK)
        //구글, 카카오, 네이버 각각 다르기 때문에 변수로 받아서 넣음
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        //소셜 로그인에서 API 가 제공하는 userInfo 의 Json 값(OAuth2 서비스의 유저 정보)
        Map<String, Object> attributes = oAuth2User.getAttributes();

        //socialType 에 따라 유저 정보를 통해 OAuthAttributes 객체 생성
        OAuthAttributes extractAttributes = OAuthAttributes.of(loginType, userNameAttributeName, attributes);

        Member createdMember = getMember(extractAttributes, loginType);

        //DefaultOAuth2User 를 구현한 CustomOAuth2User 객체 생성 후 반환
        return new CustomOAuth2User(
                Collections.singletonList(new SimpleGrantedAuthority(createdMember.getAuthority().getKey())),
                attributes,
                extractAttributes.getNameAttributeKey(),
                createdMember.getMemberId(),
                createdMember.getOauthId()
        );
    }

    /**
     * registrationId("naver", "kakao", "google")에 맞는 소셜 타입 반환하는 함수
     * @param registrationId
     * @return 소셜 타입
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
     * LoginType, attributes 에 들어있는 소셜 로그인 식별값 id를 통해 회원을 찾아 반환하는 함수
     * @param attributes
     * @param loginType
     * @return 찾은 회원이 있다면 그대로 반환, 없다면 saveMember()를 호출하여 회원 저장
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
     * OAuthAttributes 의 toEntity()를 통해 builder 로 Member 객체 생성 후 DB에 저장
     * ... socialType, socialId, email, role 값만 있는 상태
     */
    @Transactional
    private Member saveMember(OAuthAttributes attributes, LoginType loginType) {
        entityUtils.checkEmailExists(attributes.getOauth2UserInfo().getEmail());    //중복 이메일 확인
        Member createdMember = attributes.toEntity(loginType, attributes.getOauth2UserInfo());
        return memberRepository.save(createdMember);
    }

}
