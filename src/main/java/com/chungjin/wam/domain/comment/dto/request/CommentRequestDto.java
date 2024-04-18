package com.chungjin.wam.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
public class CommentRequestDto {

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

}
