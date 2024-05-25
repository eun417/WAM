package com.chungjin.wam.domain.file.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class S3GetResponseDto {

    private List<String> fileUrls;

    public static S3GetResponseDto from(List<String> fileUrls) {
        return new S3GetResponseDto(fileUrls);
    }

}
