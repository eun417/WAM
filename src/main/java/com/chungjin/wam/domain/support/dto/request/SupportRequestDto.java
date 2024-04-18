package com.chungjin.wam.domain.support.dto.request;

import com.chungjin.wam.domain.support.entity.AnimalSubjects;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
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
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;
    private Boolean commentCheck;

}
