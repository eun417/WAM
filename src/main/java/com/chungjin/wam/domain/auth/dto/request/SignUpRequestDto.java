package com.chungjin.wam.domain.auth.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpRequestDto {

    private String email;
    private String password;
//    private String nickname;
    private String name;
    private String phoneNumber;

}
