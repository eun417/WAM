package com.chungjin.wam.global.jwt;

import com.chungjin.wam.domain.auth.dto.TokenDto;
import com.chungjin.wam.domain.auth.service.CustomUserDetailsService;
import com.chungjin.wam.domain.auth.service.RedisService;
import com.chungjin.wam.global.exception.CustomException;
import com.chungjin.wam.global.exception.error.ErrorCodeType;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "bearer";

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;    // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 3;  // 3일

    public static final String AUTHORIZATION_HEADER = "Authorization";
//    public static final String REFRESH_HEADER = "Refresh";
    public static final String BEARER_PREFIX = "Bearer ";

    private final CustomUserDetailsService userDetailsService;
    private final RedisService redisService;

    private final Key key;

    /**
     * 주어진 시크릿 키를 사용하여 JwtTokenProvider 인스턴스를 생성하고 초기화
     * 시크릿 키는 JWT 토큰의 생성 및 검증에 사용
     * @param secretKey
     * @param userDetailsService
     * @param redisService
     */
    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey, CustomUserDetailsService userDetailsService, RedisService redisService) {
        //Base64로 인코딩된 시크릿 키를 디코딩
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);

        //주어진 바이트 배열로부터 HMAC SHA 키를 생성. 이 키는 JWT 토큰의 서명에 사용
        this.key = Keys.hmacShaKeyFor(keyBytes);

        this.userDetailsService = userDetailsService;
        this.redisService = redisService;
    }

    /**
     * 사용자의 로그인에 의해 생성된 인증 정보를 기반으로 access token, refresh token 을 생성하여 반환
     */
    public TokenDto generateTokenDto(Authentication authentication) {
        //권한들 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        //Access Token 생성
        String accessToken = generateAccessToken(authentication.getName(), authorities);
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);

        //Refresh Token 생성
        String refreshToken = generateRefreshToken(authentication.getName(), authorities);

        //TokenDto 생성: 클라이언트에게 제공되어야 하는 토큰 정보 담고 있음
        return TokenDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
                .refreshToken(refreshToken)
                .build();
    }

    //Access Token 생성하는 함수
    public String generateAccessToken(String memberIdString, String authorities) {
        long now = (new Date()).getTime();
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        return Jwts.builder()
                .setSubject(memberIdString)   //payload "sub": "name" ... 사용자 이름
                .claim(AUTHORITIES_KEY, authorities)    //payload "auth": "ROLE_USER" ... 사용자 권한 정보
                .setExpiration(accessTokenExpiresIn)    //payload "exp": 1516239022(예시) ... 액세스 토큰 만료 시간
                .signWith(key, SignatureAlgorithm.HS512)    //header "alg": "HS512" ... JWT에 서명 추가
                .compact(); //JWT를 문자로 직렬화하여 반환
    }

    //Refresh Token 생성하는 함수
    private String generateRefreshToken(String memberIdString, String authorities) {
        long now = (new Date()).getTime();
        String refreshToken = Jwts.builder()
                .setSubject(memberIdString) //memberId 포함
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
        List<String> values = new ArrayList<>();
        values.add(refreshToken);
        values.add(authorities);
        //Refresh Token 만료를 위해 Redis에 저장
        redisService.setListDataExpire(memberIdString, values, REFRESH_TOKEN_EXPIRE_TIME);
        return refreshToken;
    }

    /**
     * access token 을 사용하여 사용자의 인증 정보를 가져와서 Authentication 객체로 변환
     * @param accessToken
     * @return 사용자의 인증 정보
     */
    public Authentication getAuthentication(String accessToken) {
        //accessToken 을 파싱하여 클레임 가져오기
        Claims claims = parseClaims(accessToken);

        //권한 정보가 없는 경우 에러 발생
        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        //클레임에서 가져온 권한 정보를 문자열로 파싱하여 GrantedAuthority 객체들의 컬렉션을 생성
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        //클레임에서 주체(subject)로 사용자 정보를 추출하고, 사용자 상세 정보를 불러오는 UserDetails 가져오기
        UserDetails principal = userDetailsService.loadUserByUsername(claims.getSubject());

        /*
         사용자의 인증 정보
         비밀번호는 비어있는 문자열로, 권한 정보는 앞서 변환한 authorities 로 설정
         ""(비밀번호): JWT 토큰을 사용하여 사용자를 인증하고 권한을 부여하는 경우에는 이미 인증이 완료되었으며,
         사용자의 비밀번호는 필요하지 않음
        */
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    /**
     * 필터링하기 위해선 토큰 정보 필요
     * -> Request Header에서 토큰 정보를 꺼내옴
     * @param request
     * @return access token OR null
     */
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }

        return null;
    }

    /**
     * JWT 토큰에서 memberId 조회
     */
    public String getMemberId(String token) {
        return parseClaims(token).getSubject();
    }

    /**
     * access token 의 서명 검증, 유효기간을 확인하여 유효성 검사
     */
    public boolean validateToken(String token) {
        try {
            //유효한 토큰인 경우에만 클레임 반환
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
            throw new CustomException(ErrorCodeType.WRONG_TYPE_SIGNATURE);
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
            throw new CustomException(ErrorCodeType.ACCESS_TOKEN_EXPIRED);
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
            throw new CustomException(ErrorCodeType.INVALID_TOKEN);
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    /**
     * refresh token 유효성 검사
     */
    public void validateRefreshToken(String refreshToken) {
        try {
            //유효한 토큰인 경우에만 클레임 반환
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(refreshToken);
        } catch (SecurityException | MalformedJwtException e) {
            throw new CustomException(ErrorCodeType.WRONG_TYPE_SIGNATURE);
        } catch (ExpiredJwtException e) {
            throw new CustomException(ErrorCodeType.REFRESH_TOKEN_EXPIRED);
        } catch (UnsupportedJwtException e) {
            throw new CustomException(ErrorCodeType.INVALID_TOKEN);
        }
    }

    /**
     * access token 을 파싱하여 그 안에 포함된 클레임을 추출하고, 만료된 토큰인 경우에는 만료된 토큰의 클레임을 반환
     * @param token
     * @return Claims: name-value 형태인 정보의 조각들
     */
    public Claims parseClaims(String token){
        try{
            /*
             setSigningKey(key): JwtParser 에 대한 서명 키를 설정(JWT 토큰을 생성할 때 사용된 서명 키와 일치)
             ->  JWT 토큰의 유효성 검증
             parseClaimsJws(accessToken): 주어진 액세스 토큰을 파싱하여 클레임을 추출
             ... JWT 토큰의 서명을 검증하고, 서명이 유효한 경우에만 클레임을 추출
             */
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException ex){    //JWT 토큰이 만료된 경우에 발생하는 예외
            return ex.getClaims();
        }
    }

//    /**
//     * 응답 메시지에 AccessToken 헤더로 보내기
//     */
//    public void sendAccessToken(HttpServletResponse response, String accessToken) {
//        response.setStatus(HttpServletResponse.SC_OK);
//        response.setHeader(AUTHORIZATION_HEADER, accessToken);
//        log.info("재발급된 Access Token : {}", accessToken);
//    }
//
//    /**
//     * 응답 메시지에 AccessToken + RefreshToken 헤더로 보내기
//     */
//    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
//        response.setStatus(HttpServletResponse.SC_OK);
//        response.setHeader(AUTHORIZATION_HEADER, accessToken);
//        response.setHeader(REFRESH_HEADER, refreshToken);
//        log.info("Access Token, Refresh Token 헤더 설정 완료");
//    }

}
