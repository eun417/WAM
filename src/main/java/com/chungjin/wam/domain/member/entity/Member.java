package com.chungjin.wam.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {

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

    @Column(name = "create_date", updatable = false)  //업데이트 되지 않도록 설정
    private String createDate;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    @PrePersist
    protected void onCreate() {
        //엔터티가 영속화되기 전에 현재 날짜로 초기화
        createDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
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
