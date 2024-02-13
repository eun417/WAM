package com.chungjin.wam.domain.qna.dto;

import com.chungjin.wam.domain.qna.entity.QnaCheck;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QnaAnswerRequestDto {

    private Long qnaId;
    private String answer;
    private String answerDate;
    @Enumerated(EnumType.STRING)
    private QnaCheck qnaCheck;

}
