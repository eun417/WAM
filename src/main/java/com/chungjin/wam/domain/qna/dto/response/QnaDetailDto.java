package com.chungjin.wam.domain.qna.dto.response;

import com.chungjin.wam.domain.qna.entity.QnaCheck;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QnaDetailDto {

    private Long qnaId;
    private Long memberId;
    private String nickname;
    private String title;
    private String content;
    private String createDate;
    private Long viewCount;
    private String answer;
    private String answerDate;
    @Enumerated(EnumType.STRING)
    private QnaCheck qnaCheck;

}
