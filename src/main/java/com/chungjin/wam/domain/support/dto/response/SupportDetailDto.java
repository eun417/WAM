package com.chungjin.wam.domain.support.dto.response;

import com.chungjin.wam.domain.comment.dto.response.CommentDto;
import com.chungjin.wam.domain.support.entity.AnimalSubjects;
import com.chungjin.wam.domain.support.entity.SupportStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SupportDetailDto {

    private Long supportId;
    private Long memberId;
    private AnimalSubjects animalSubjects;
    private String title;
    private Long goalAmount;
    @Enumerated(EnumType.STRING)
    private SupportStatus supportStatus;
    private String startDate;
    private String endDate;
    private String firstImg;
    private String content;
    private Long supportLike;
    private Long supportAmount;
    private Boolean commentCheck;

    private List<CommentDto> comments;

}
