package com.chungjin.wam.domain.member.dto.response;

import com.chungjin.wam.domain.support.entity.SupportStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyLikeResponseDto {

    private Long supportId;
    private Long supportLikeId;
    private String title;
    private String nickname;
    private String createDate;
    @Enumerated(EnumType.STRING)
    private SupportStatus supportStatus;

}
