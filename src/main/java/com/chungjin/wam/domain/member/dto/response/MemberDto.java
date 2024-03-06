package com.chungjin.wam.domain.member.dto.response;

import com.chungjin.wam.domain.member.entity.Authority;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

    private Long memberId;
    private String email;
//    private String password;
    private String nickname;
    private String name;
    private String phoneNumber;
    private String createDate;
    @Enumerated(EnumType.STRING)
    private Authority authority;

}
