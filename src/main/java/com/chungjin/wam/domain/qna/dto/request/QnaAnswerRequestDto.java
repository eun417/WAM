package com.chungjin.wam.domain.qna.dto.request;

import com.chungjin.wam.domain.qna.entity.QnaCheck;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QnaAnswerRequestDto {

    @NotBlank(message = "내용을 입력해주세요.")
    private String answer;
}
