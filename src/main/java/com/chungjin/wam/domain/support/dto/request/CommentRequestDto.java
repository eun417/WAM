package com.chungjin.wam.domain.support.dto.request;

import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {

    private Long commentId;
    private String content;
    private String createDate;
    private Long memberId;

}
