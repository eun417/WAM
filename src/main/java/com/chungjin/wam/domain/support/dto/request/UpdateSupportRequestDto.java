package com.chungjin.wam.domain.support.dto.request;

import com.chungjin.wam.domain.support.entity.AnimalSubjects;
import com.chungjin.wam.domain.comment.entity.CommentCheck;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class UpdateSupportRequestDto {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;
    private AnimalSubjects animalSubjects;
    @NotNull(message = "목표 금액을 입력해주세요.")
    private int goalAmount;
    @NotBlank(message = "시작일을 선택해주세요.")
    private String startDate;
    @NotBlank(message = "종료일을 선택해주세요.")
    private String endDate;
    @NotNull(message = "대표이미지를 선택해주세요.")
    private Boolean firstImgDeleted;    //기존 대표 이미지 삭제 여부(삭제하면 true / 기본값 false)
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;
    private Boolean commentCheck;

}
