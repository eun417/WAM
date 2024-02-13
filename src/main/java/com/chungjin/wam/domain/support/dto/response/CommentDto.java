package com.chungjin.wam.domain.support.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Long commentId;
    private String email;
    private String content;
    private String createDate;

}
