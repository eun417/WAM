package com.chungjin.wam.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

import static com.chungjin.wam.global.util.RegExp.PASSWORD_REGEXP;

@Getter
public class ChangePwRequestDto {

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = PASSWORD_REGEXP, message = "비밀번호는 영문, 숫자, 특수문자 조합으로 이루어진 8~15자만 입력 가능합니다")
    private String newPassword;

    @NotBlank(message = "비밀번호를 확인해주세요.")
    private String checkPassword;

}
