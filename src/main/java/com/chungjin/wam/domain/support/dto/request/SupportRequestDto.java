package com.chungjin.wam.domain.support.dto.request;

import com.chungjin.wam.domain.support.entity.AnimalSubjects;
import com.chungjin.wam.domain.comment.entity.CommentCheck;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SupportRequestDto {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;
    private AnimalSubjects animalSubjects;
    @NotNull(message = "목표 금액을 입력해주세요.")
    private Long goalAmount;
    @NotBlank(message = "시작일을 선택해주세요.")
    private String startDate;
    @NotBlank(message = "종료일을 선택해주세요.")
    private String endDate;
    @NotBlank(message = "대표이미지를 선택해주세요.")
    private String firstImg;
    private String subheading;
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;
    @Enumerated(EnumType.STRING)
    private CommentCheck commentCheck;

}
