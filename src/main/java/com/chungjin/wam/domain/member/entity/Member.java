package com.chungjin.wam.domain.member.entity;

import com.chungjin.wam.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

    //비밀번호 변경
    public void updatePw(String newPassword) {
        this.password = newPassword;
    }

    //권한 변경
    public void updateAuthority(Authority authority) {
        this.authority = authority;
    }


}
