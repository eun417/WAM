package com.chungjin.wam.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.chungjin.wam.global.util.RegExp.PHONE_NUMBER_REGEXP;

@Getter
@Setter
@NoArgsConstructor
public class FindEmailRequestDto {

    @NotBlank(message = "휴대폰 번호를 입력해주세요.")
    @Pattern(regexp = PHONE_NUMBER_REGEXP, message = "숫자만 입력하세요.")
    private String phoneNumber;

    @NotBlank
    private String name;

}
