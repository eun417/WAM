package com.chungjin.wam.domain.member.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

import static com.chungjin.wam.global.util.Constants.PHONE_NUMBER_REGEXP;

@Getter
public class UpdateMemberRequestDto {

    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;
    private String name;
    @Nullable
    @Pattern(regexp = PHONE_NUMBER_REGEXP, message = "휴대폰 번호는 숫자만 입력하세요.")
    private String phoneNumber;

}
