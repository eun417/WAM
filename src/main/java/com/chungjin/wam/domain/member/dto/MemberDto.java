package com.chungjin.wam.domain.member.dto;

import com.chungjin.wam.domain.member.entity.Member;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

    private Long memberId;
    private String email;
    private String password;
    private String nickname;
    private String name;
    private String phoneNumber;
    private String createDate;

}
