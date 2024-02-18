package com.chungjin.wam.domain.member.dto.response;

import com.chungjin.wam.domain.qna.entity.QnaCheck;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MyQnaResponseDto {

    private Long qnaId;
    private String title;
    private String createDate;
    @Enumerated(EnumType.STRING)
    private QnaCheck qnaCheck;

}
