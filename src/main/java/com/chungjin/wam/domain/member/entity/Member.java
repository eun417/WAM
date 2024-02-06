package com.chungjin.wam.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
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

}
