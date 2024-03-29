package com.chungjin.wam.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class TokenRequestDto {

    @NotBlank(message = "Refresh Token이 존재하지 않습니다.")
    private String refreshToken;
}
