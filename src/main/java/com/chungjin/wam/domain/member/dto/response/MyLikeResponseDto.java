package com.chungjin.wam.domain.member.dto.response;

import com.chungjin.wam.domain.support.entity.Support;
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

    public static MyLikeResponseDto toDto(Support support) {
        return MyLikeResponseDto.builder()
                .supportId(support.getSupportId())
                .title(support.getTitle())
                .nickname(support.getMember().getNickname())
                .createDate(support.getCreateDate())
                .supportStatus(support.getSupportStatus())
                .build();
    }

}
