package com.chungjin.wam.domain.qna.dto.request;

import com.chungjin.wam.domain.qna.entity.QnaCheck;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@NoArgsConstructor
public class QnaRequestDto {

    private String title;
    private String content;

}
