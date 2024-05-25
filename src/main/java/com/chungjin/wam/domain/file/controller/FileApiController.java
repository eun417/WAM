package com.chungjin.wam.domain.file.controller;

import com.chungjin.wam.domain.file.service.FileService;
import com.chungjin.wam.global.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

import static com.chungjin.wam.global.util.Constants.S3_QNA;
import static com.chungjin.wam.global.util.Constants.S3_SUPPORT;

@RestController
@RequiredArgsConstructor
public class FileApiController {

    private final S3Service s3Service;
    private final FileService fileService;

    /**
     * 후원 - 이미지 업로드
     */
    @PostMapping("/support/image-upload")
    public ResponseEntity<String> uploadSupportImage(@RequestPart("file") MultipartFile file) {
        return ResponseEntity.ok().body(s3Service.uploadFile(file, S3_SUPPORT + "/" + LocalDate.now()));
    }

    /**
     * 후원 - 이미지 삭제
     */
    @PostMapping("/support/image-delete")
    public ResponseEntity<String> deleteSupportImage(@RequestPart("fileUrl") String fileUrl) {
        s3Service.deleteImage(fileUrl);
        return ResponseEntity.ok().body("이미지 삭제 완료");
    }

    /**
     * QnA - 이미지 업로드
     */
    @PostMapping("/qna/image-upload")
    public ResponseEntity<String> uploadQnAImage(@RequestPart("file") MultipartFile file) {
        return ResponseEntity.ok().body(s3Service.uploadFile(file, S3_QNA + "/" + LocalDate.now()));
    }

    /**
     * QnA - 이미지 삭제
     */
    @PostMapping("/qna/image-delete")
    public ResponseEntity<String> deleteQnAImage(@RequestPart("fileUrl") String fileUrl) {
        s3Service.deleteImage(fileUrl);
        return ResponseEntity.ok().body("이미지 삭제 완료");
    }

}
