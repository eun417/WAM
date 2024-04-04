package com.chungjin.wam.domain.member.entity;

import com.chungjin.wam.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  //의미 없는 객체 생성 막음
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    private String oauthId; //로그인한 소셜 타입의 식별자 값 (일반 로그인인 경우 null)

    private String email;

    private String password;

    private String nickname;

    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    //생성자에 @Builder 선언
    @Builder
    public Member(String oauthId, String email, String password, String name, String phoneNumber, Authority authority, String nickname, LoginType loginType) {
        this.oauthId = oauthId;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.authority = authority;
        this.nickname = nickname;
        this.loginType = loginType;
    }

    //비밀번호 변경
    public void updatePw(String newPassword) {
        this.password = newPassword;
    }

    //권한 변경
    public void updateAuthority(Authority authority) {
        this.authority = authority;
    }

}
