package com.chungjin.wam.domain.member.dto.response;

import com.chungjin.wam.domain.support.entity.SupportStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MySupportResponseDto {

    private Long supportId;
    private String title;
    private Long goalAmount;
    private Long supportAmount;
    private String createDate;
    @Enumerated(EnumType.STRING)
    private SupportStatus supportStatus;

}
