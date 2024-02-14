package com.chungjin.wam.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;

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

    private String email;

    private String password;

    private String nickname;

    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "create_date")
    private String createDate;

    @Enumerated(EnumType.STRING)
    private Authority authority;

}
