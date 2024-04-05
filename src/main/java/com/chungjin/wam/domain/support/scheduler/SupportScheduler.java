package com.chungjin.wam.domain.support.scheduler;

import com.chungjin.wam.domain.support.entity.Support;
import com.chungjin.wam.domain.support.entity.SupportStatus;
import com.chungjin.wam.domain.support.repository.SupportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.chungjin.wam.domain.support.entity.SupportStatus.END;
import static com.chungjin.wam.domain.support.entity.SupportStatus.ENDING_SOON;

@Component
@RequiredArgsConstructor
public class SupportScheduler {

    private final SupportRepository supportRepository;

    /**
     * 매일 자정에 실행, 후원 마감일이 24시간 전인 게시물의 상태를 "종료임박"으로 변경
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void checkSupportExpirations() {
        //현재 시간에서 24시간 이후 시간
        String twentyFourHoursAfter = LocalDateTime.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));

        //24시간 이내의 후원을 찾아 상태를 변경
        List<Support> expiringSupports = supportRepository.findByDueDateBeforeAndStatusNot(twentyFourHoursAfter, SupportStatus.END);

        for (Support support : expiringSupports) {
            support.updateSupportStatus(ENDING_SOON);   //"종료임박"으로 수정
        }
    }

}
