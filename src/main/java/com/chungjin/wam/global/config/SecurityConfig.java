package com.chungjin.wam.global.config;

import com.chungjin.wam.domain.auth.handler.OAuth2LoginFailureHandler;
import com.chungjin.wam.domain.auth.handler.OAuth2LoginSuccessHandler;
import com.chungjin.wam.domain.auth.service.CustomOAuth2UserService;
import com.chungjin.wam.global.jwt.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
//    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;

    private static final String[] ALLOWED_URLS = {
            //추후 수정 필요
            "/**",
            "/auth/**", "/oauth/**",
            "/qna/**", "/support/**", "/members/**", "/email/**",
            "/h2-console/**", "/resources/**"
    };

    private static final String[] ADMIN_URLS = {
//            "PUT:/qna/**/answer"
    };

    /**
     * 정적 자원에 대해 보안을 적용하지 않도록 설정
     * 정적 자원은 보통 HTML, CSS, JavaScript, 이미지 파일 등을 의미, 이들에 대해 보안을 적용하지 않음으로써 성능을 향상시킬 수 있음
     */
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return web -> web.ignoring()
//                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
//    }

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
                //token을 사용하는 방식이기 때문에 csrf를 disable
                .csrf(AbstractHttpConfigurer::disable)

                //exception을 핸들링할 때, 만들었던 클래스 추가
                .exceptionHandling(exceptionHandling -> exceptionHandling
//                        .accessDeniedHandler(jwtAccessDeniedHandler)
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint))

                //HttpServletRequest를 사용하는 요청들에 대한 접근 제한 설정
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers(ALLOWED_URLS).permitAll()  //인증 없이 접근 허용
                        .requestMatchers(PathRequest.toH2Console()).permitAll()
//                        .requestMatchers(ADMIN_URLS).hasRole("ADMIN")
                        .anyRequest().authenticated())   //나머지 요청들은 모두 인증 필요


                //세션을 사용하지 않기 때문에 STATELESS로 설정
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                //h2-console 설정
                .headers(headers ->
                        headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))

                //JwtFilter를 addFilterBefore()로 등록했던 JwtSecurityConfig 클래스도 적용
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

//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(customUserDetailService).passwordEncoder(passwordEncoder());
//        return auth.build();
//    }

}
