package com.chungjin.wam.domain.qna.dto.response;

import com.chungjin.wam.domain.qna.entity.QnaCheck;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QnaResponseDto {

    private Long qnaId;
    private String title;
    private String nickname;
    private String createDate;
    private Long viewCount;
    @Enumerated(EnumType.STRING)
    private QnaCheck qnaCheck;

}
