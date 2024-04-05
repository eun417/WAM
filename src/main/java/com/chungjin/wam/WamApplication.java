package com.chungjin.wam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling	//Scheduler 활성화
@SpringBootApplication
public class WamApplication {

	public static void main(String[] args) {
		SpringApplication.run(WamApplication.class, args);
	}

}
