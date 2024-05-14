package com.chungjin.wam.global.common;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class TimeTraceAop {

    @Around("bean(*Controller)")    //Controller로 이름이 끝나는 모든 빈에 적용
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        log.info("START: " + joinPoint.toString());   //어떤 메소드를 콜하는지 이름 보여줌

        try {
            return joinPoint.proceed(); //다음 메소드로 진행
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            log.info("END: " + joinPoint.toString() + " " + timeMs + "ms");
        }
    }

}
