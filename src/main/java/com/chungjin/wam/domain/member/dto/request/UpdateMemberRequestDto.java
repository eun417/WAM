package com.chungjin.wam.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.chungjin.wam.global.util.RegExp.PHONE_NUMBER_REGEXP;

@Getter
@Setter
@NoArgsConstructor
public class UpdateMemberRequestDto {

    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;
    @NotBlank(message = "이름을 입력해주세요.")
    private String name;
    @NotBlank(message = "휴대폰 번호를 입력해주세요.")
    @Pattern(regexp = PHONE_NUMBER_REGEXP, message = "휴대폰 번호는 숫자만 입력하세요.")
    private String phoneNumber;

}
