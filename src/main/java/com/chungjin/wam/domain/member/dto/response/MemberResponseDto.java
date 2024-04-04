package com.chungjin.wam.domain.member.dto.response;

import com.chungjin.wam.domain.member.entity.Authority;
import lombok.*;

@Getter
@Builder
public class MemberResponseDto {

    private Long memberId;
    private String email;
    private String nickname;
    private String name;
    private String phoneNumber;
    private String createDate;
    private Authority authority;

}
