package com.chungjin.wam.domain.file.scheduler;

import com.chungjin.wam.domain.file.dto.S3GetResponseDto;
import com.chungjin.wam.domain.file.repository.FileRepository;
import com.chungjin.wam.global.s3.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import static com.chungjin.wam.global.util.Constants.S3_QNA;
import static com.chungjin.wam.global.util.Constants.S3_SUPPORT;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileScheduler {

    private final S3Service s3Service;

    private final FileRepository fileRepository;

    @Transactional
    @Scheduled(cron = "0 0 0 ? * MON")  //매주 월요일 자정
    public void deleteUnnecessaryFiles() {
        log.info("불필요한 파일 삭제 Scheduler 실행");

        LocalDate now = LocalDate.now();
        LocalDate lastMonday = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).minusWeeks(1);
        LocalDate lastSunday = lastMonday.plusDays(6);

        //지난 주 월요일부터 일요일까지의 날짜 범위
        List<String> dateRange = getLastWeekDateRange(lastMonday, lastSunday);

        //지난 주 동안 업로드된 모든 파일 URL 조회
        List<String> allFileUrls = new ArrayList<>();
        for (String date : dateRange) {
            allFileUrls.addAll(getFileUrlsForDate(date, S3_SUPPORT));
            allFileUrls.addAll(getFileUrlsForDate(date, S3_QNA));
        }
        log.info("첫 번째로 가져온 URL : {}", allFileUrls.get(0));

        //날짜 범위에 해당하는 uploadPath 조회
        List<String> uploadPaths = fileRepository.findUploadPathsByDateRange(lastMonday, lastSunday);

        allFileUrls.removeAll(uploadPaths); //IMAGE 테이블에 있는 이미지들은 allFileUrls에서 제외

        //allFileUrls에 남은 항목들을 S3에서 삭제
        for (String fileUrl : allFileUrls) {
            s3Service.deleteImage(fileUrl);
        }

        log.info("불필요한 파일 삭제 완료");
    }

    //시작날 ~ 마지막날까지 날짜들 구하는 함수
    private List<String> getLastWeekDateRange(LocalDate startDate, LocalDate endDate) {
        List<String> dateList = new ArrayList<>();

        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            dateList.add(currentDate.toString());
            currentDate = currentDate.plusDays(1);
        }

        return dateList;
    }

    //특정 날짜, 폴더에 해당하는 파일 URL 조회 함수
    private List<String> getFileUrlsForDate(String date, String folder) {
        S3GetResponseDto s3GetResponseDto = s3Service.findFiles(folder + "/" + date);
        return s3GetResponseDto.getFileUrls();
    }

}
