package com.chungjin.wam.domain.support.scheduler;

import com.chungjin.wam.domain.support.entity.Support;
import com.chungjin.wam.domain.support.entity.SupportStatus;
import com.chungjin.wam.domain.support.repository.SupportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SupportScheduler {

    private final SupportRepository supportRepository;

    /**
     * 매일 자정에 실행
     */
    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void checkSupportExpirations() {
        changeStatusEndingSoon();   //END 로 변경
        changeStatusEnd();  //ENDING_SOON 으로 변경
    }

    //마감일 지난 후원의 상태를 END 로 변경
    public void changeStatusEnd() {
        String yesterday = LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));

        //마감일이 지난 후원 목록을 조회
        List<Support> expiringSupports = supportRepository.findByEndDateAndSupportStatusNot(yesterday, SupportStatus.END);

        for (Support support : expiringSupports) {
            support.setSupportStatus(SupportStatus.END); //후원 상태를 "END"로 변경
        }
    }

    //마감일이 하루 남은 후원의 상태를 ENDING_SOON 으로 변경
    public void changeStatusEndingSoon() {
        //현재 시간
        String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));

        //24시간 이내의 후원을 찾아 상태를 변경
        List<Support> expiringSupports = supportRepository.findByEndDateAndSupportStatusNot(today, SupportStatus.END);

        for (Support support : expiringSupports) {
            support.updateSupportStatus(SupportStatus.ENDING_SOON);   //"종료임박"으로 수정
        }
    }

}
