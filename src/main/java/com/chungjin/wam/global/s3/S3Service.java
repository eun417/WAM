package com.chungjin.wam.global.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.chungjin.wam.domain.file.dto.S3GetResponseDto;
import com.chungjin.wam.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
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
     * @param file
     * @param directory
     * @return 저장된 이미지의 주소
     */
    public String uploadFile(MultipartFile file, String directory) {
        if (file.isEmpty()) {
            throw new CustomException(RESOURCE_NOT_FOUND);
        }

        String fileName = directory + "/" + createFileName(file.getOriginalFilename());
        log.info("upload fileName: " + fileName);

        //S3에 업로드되는 파일 또는 객체와 관련된 정보(파일의 유형, 크기, 버전 등과 같은 정보)
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
     * @param fileUrl
     */
    public void deleteImage(String fileUrl) {
        log.info("delete fileUrl: " + fileUrl);

        try {
            //fileName 문자열에서 url 문자열의 인덱스 찾음
            int index = fileUrl.indexOf(url);
            //url 이후의 파일 경로(key) 추출
            String fileRoute = fileUrl.substring(index + url.length());

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

    /**
     * S3에서 객체 조회
     * @param prefix
     * @return 조회된 파일 URL List
     */
    public S3GetResponseDto findFiles(String prefix) {
        List<String> fileUrls = new ArrayList<>();

        //S3 객체 목록 요청 위한 객체 생성
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                .withBucketName(bucket) //버킷 이름 설정
                .withPrefix(prefix.isEmpty() ? null : prefix + "/") //null 이면  버킷 내 모든 객체, 아니면 지정된 접두사로 시작하는 객체만 검색
                .withDelimiter("/"); //디렉토리 구조 구분

        //S3에서 객체 목록 요청
        ObjectListing objectListing;
        do {
            objectListing = s3Client.listObjects(listObjectsRequest);

            //prefix 경로 아래 파일 URL 가져와 리스트 추가
            for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                String key = objectSummary.getKey();
                String fileUrl = s3Client.getUrl(bucket, key).toString(); //객체의 URL 가져오기
                fileUrls.add(fileUrl);
            }

            //다음 페이지 요청을 위한 마커 설정
            listObjectsRequest.setMarker(objectListing.getNextMarker());
        } while (objectListing.isTruncated()); //true 일 때 반복

        return S3GetResponseDto.from(fileUrls);
    }

    //파일 이름 생성 함수
    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    //파일 확장자 구하는 함수
    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException se) {
            throw new CustomException(INVALID_FILE_FORMAT);
        }
    }

}
