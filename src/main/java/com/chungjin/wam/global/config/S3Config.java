package com.chungjin.wam.global.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    /**
     * Amazon S3 클라이언트를 생성, 빈으로 등록
     * -> 애플리케이션에서 Amazon S3를 사용 가능
     * @return AmazonS3Client: 이미지를 S3에 저장하기 위해 사용되는 객체
     */
    @Bean
    public AmazonS3Client amazonS3Client() {
        //Amazon S3 클라이언트에 사용될 기본 AWS 자격 증명을 생성
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        //Amazon S3 클라이언트를 생성(기본 설정)
        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withRegion(region) //호스팅 지역
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))  //기본 자격 증명 공급자 설정
                .build();
    }

}
