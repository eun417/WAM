package com.chungjin.wam.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.chungjin.wam.global.util.RegExp.PASSWORD_REGEXP;
import static com.chungjin.wam.global.util.RegExp.PHONE_NUMBER_REGEXP;

@Getter
@NoArgsConstructor
public class ChangePwRequestDto {

    @NotBlank
    private String email;

    @NotBlank
    @Pattern(regexp = PHONE_NUMBER_REGEXP, message = "휴대폰 번호는 10~11자리의 숫자로만 입력 가능합니다.")
    private String phoneNumber;

    @NotBlank
    @Pattern(regexp = PASSWORD_REGEXP, message = "비밀번호는 8~16 자리의 영문 & 숫자 조합으로만 입력 가능합니다.")
    private String newPassword;

    @NotBlank
    @Pattern(regexp = PASSWORD_REGEXP, message = "비밀번호는 8~16 자리의 영문 & 숫자 조합으로만 입력 가능합니다.")
    private String confirmPassword;

}
