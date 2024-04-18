package com.chungjin.wam.domain.comment.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentDto {

    private Long commentId;
    private Long memberId;
    private String nickname;
    private String content;
    private String createDate;

}
