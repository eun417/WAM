package com.chungjin.wam.global.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.chungjin.wam.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static com.chungjin.wam.global.exception.error.ErrorCodeType.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.url}")
    private String url;

    /**
     * S3에 이미지 업로드
     */
    public String uploadFile(MultipartFile file, String directory) {
        String fileName = directory + "/" + createFileName(file.getOriginalFilename());
        log.info("upload fileName: " + fileName);

        if (file.isEmpty()) {
            throw new CustomException(RESOURCE_NOT_FOUND);
        }

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());

        try (InputStream inputStream = file.getInputStream()) {
            byte[] bytes = IOUtils.toByteArray(inputStream);    //바이트 배열 생성
            objectMetadata.setContentLength(bytes.length);  //파일의 내용 길이를 바이트 배열의 길이로 설정
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);    //입력 스트림 생성

            //S3에 업로드
            s3Client.putObject(new PutObjectRequest(bucket, fileName, byteArrayInputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

        } catch (IOException e) {
            throw new CustomException(UPLOAD_FILE_FAILED);
        }

        return s3Client.getUrl(bucket, fileName).toString();
    }

    /**
     * S3에서 이미지 삭제
     */
    public void deleteImage(String fileName) {
        log.info("delete fileName: " + fileName);

        try {
            //fileName 문자열에서 url 문자열의 인덱스 찾음
            int index = fileName.indexOf(url);
            //url 이후의 파일 경로(key) 추출
            String fileRoute = fileName.substring(index + url.length());

            //객체의 존재 여부를 확인
            boolean isObjectExist = s3Client.doesObjectExist(bucket, fileRoute);
            //파일이 존재하는 경우에만 S3에서 해당 파일을 삭제
            if (isObjectExist) {
                s3Client.deleteObject(bucket, fileRoute);
            }
        } catch (Exception e) {
            throw new CustomException(DELETE_FILE_FAILED);
        }
    }

    //파일 이름 생성 메소드
    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    //파일 확장자 구하는 메소드
    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException se) {
            throw new CustomException(INVALID_FILE_FORMAT);
        }
    }

}
