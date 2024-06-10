package com.chungjin.wam.domain.qna.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Getter
public class QnaRequestDto {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;
    private List<String> fileUrls;  //본문 첨부한 이미지 URL

}
