package com.chungjin.wam.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class TokenRequestDto {

    @NotBlank
    private String accessToken;
    @NotBlank
    private String refreshToken;

}
