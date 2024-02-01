package com.chungjin.wam.domain.qna.dto;

import com.chungjin.wam.domain.qna.entity.Qna;
import com.chungjin.wam.domain.qna.entity.QnaCheck;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QnaDto {

    private Long qnaId;
    private String email;
    private String nickname;
    private String title;
    private String content;
    private String createDate;
    private int viewCount;
    private String answer;
    private String answerDate;
    @Enumerated(EnumType.STRING)
    private QnaCheck qnaCheck;

}
