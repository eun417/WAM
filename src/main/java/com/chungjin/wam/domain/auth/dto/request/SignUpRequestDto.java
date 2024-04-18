package com.chungjin.wam.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

import static com.chungjin.wam.global.util.Constants.PASSWORD_REGEXP;
import static com.chungjin.wam.global.util.Constants.PHONE_NUMBER_REGEXP;

@Getter
public class SignUpRequestDto {

    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = PASSWORD_REGEXP, message = "영문, 숫자, 특수문자 조합으로 이루어진 8~15자로 입력하세요.")
    private String password;
    @NotBlank(message = "비밀번호를 확인해주세요.")
    @Pattern(regexp = PASSWORD_REGEXP, message = "영문, 숫자, 특수문자 조합으로 이루어진 8~15자로 입력하세요.")
    private String checkPassword;
    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;
    @NotBlank(message = "이름을 입력해주세요.")
    private String name;
    @NotBlank(message = "휴대폰 번호를 입력해주세요.")
    @Pattern(regexp = PHONE_NUMBER_REGEXP, message = "숫자만 입력하세요.")
    private String phoneNumber;

}
