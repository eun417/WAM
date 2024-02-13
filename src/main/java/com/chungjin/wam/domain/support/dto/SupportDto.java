package com.chungjin.wam.domain.support.dto;

import com.chungjin.wam.domain.support.entity.CommentCheck;
import com.chungjin.wam.domain.support.entity.SupportStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupportDto {

    private Long supportId;
    private String email;
    private Long animalId;
    private String title;
    private Long goalAmount;
    @Enumerated(EnumType.STRING)
    private SupportStatus supportStatus;
    private String startDate;
    private String endDate;
    private String firstImg;
    @Enumerated(EnumType.STRING)
    private CommentCheck commentCheck;
    private String subheading;
    private String content;
    private Long supportLike;
    private Long supportAmount;

}
