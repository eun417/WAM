package com.chungjin.wam.domain.qna.dto.response;

import com.chungjin.wam.domain.qna.entity.QnaCheck;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QnaDetailDto {

    private Long qnaId;
    private String email;
    private String title;
    private String content;
    private String createDate;
    private int viewCount;
    private String answer;
    private String answerDate;
    @Enumerated(EnumType.STRING)
    private QnaCheck qnaCheck;

}
