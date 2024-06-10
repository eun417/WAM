package com.chungjin.wam.domain.support.dto.response;

import com.chungjin.wam.domain.support.entity.SupportStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@Builder
public class SupportResponseDto {

    private Long supportId;
    private String title;
    private String nickname;
    @Enumerated(EnumType.STRING)
    private SupportStatus supportStatus;
    private String firstImg;
    private Long goalAmount;
    private Long supportAmount;

}
