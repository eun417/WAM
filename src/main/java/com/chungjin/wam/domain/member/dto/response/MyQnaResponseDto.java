package com.chungjin.wam.domain.member.dto.response;

import com.chungjin.wam.domain.qna.entity.QnaCheck;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyQnaResponseDto {

    private Long qnaId;
    private String title;
    private String createDate;
    private Long viewCount;
    @Enumerated(EnumType.STRING)
    private QnaCheck qnaCheck;

}
