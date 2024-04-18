package com.chungjin.wam.domain.qna.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class QnaAnswerRequestDto {

    @NotBlank(message = "내용을 입력해주세요.")
    private String answer;
}
