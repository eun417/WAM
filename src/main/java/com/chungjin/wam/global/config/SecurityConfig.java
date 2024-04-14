package com.chungjin.wam.global.config;

import com.chungjin.wam.domain.auth.handler.OAuth2LoginFailureHandler;
import com.chungjin.wam.domain.auth.handler.OAuth2LoginSuccessHandler;
import com.chungjin.wam.domain.auth.service.CustomOAuth2UserService;
import com.chungjin.wam.domain.member.entity.Authority;
import com.chungjin.wam.global.jwt.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;

    private static final String[] ALLOWED_URLS = {
            //추후 수정 필요
            "/",
            "/auth/**",
            "/qna/**",
            "/support/**",
            "/payment/**",
            "/animal-map/**",
            "/member/profile", "/member/like", "/member/support", "/member/qna", "/member/leave",
            "/admin/member/list", "/admin/support/list", "/admin/qna/list",
            "/h2-console/**",
            "/css/**", "/js/**"
    };

    /**
     * PasswordEncoder: Spring Security에서 사용자의 비밀번호를 안전하게 저장하기 위해 사용
     * 비밀번호를 해시화하여 저장하므로 원본 비밀번호 저장 X(비밀번호가 데이터베이스에서 노출되더라도 원본 비밀번호를 복원 못함)
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        //bcrypt 알고리즘을 사용하여 비밀번호를 안전하게 해시화
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                //토큰을 사용하는 방식이기 때문에 csrf 를 disable
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable)
                .formLogin(FormLoginConfigurer::disable)
                .logout(LogoutConfigurer::disable)

                //exception 을 핸들링할 때, 만들었던 클래스 추가
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint))

                //세션을 사용하지 않기 때문에 STATELESS 로 설정
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                //HttpServletRequest 를 사용하는 요청들에 대한 접근 제한 설정
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                        .requestMatchers(HttpMethod.POST, "/support/").hasAuthority(Authority.ROLE_USER.getKey())
                        .requestMatchers(HttpMethod.PUT, "/support/{supportId}").hasAuthority(Authority.ROLE_USER.getKey())
                        .requestMatchers(HttpMethod.DELETE, "/support/{supportId}").hasAuthority(Authority.ROLE_USER.getKey())
                        .requestMatchers(ALLOWED_URLS).permitAll()  //인증 없이 접근 허용
                        .requestMatchers("/admin/**").hasAuthority(Authority.ROLE_ADMIN.getKey())
                        .anyRequest().authenticated())   //나머지 요청들은 모두 인증 필요

                //h2-console 설정
                .headers(headers ->
                        headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))

                //JwtFilter 를 addFilterBefore()로 등록했던 JwtSecurityConfig 클래스도 적용
                .with(new JwtSecurityConfig(jwtTokenProvider), customizer -> {})

                //소셜 로그인 설정
                .oauth2Login(configure -> configure
                                .userInfoEndpoint(config -> config.userService(customOAuth2UserService))
                                .successHandler(oAuth2LoginSuccessHandler)
                                .failureHandler(oAuth2LoginFailureHandler)
                )
        ;

        return http.build();
    }

    /**
     * AuthenticationManager 빈 구성
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
