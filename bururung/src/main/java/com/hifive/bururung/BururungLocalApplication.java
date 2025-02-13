package com.hifive.bururung;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;

public class BururungLocalApplication {
	// 로깅 파일 저장 확인용
    private static final Logger logger = LoggerFactory.getLogger(BururungApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(BururungApplication.class, args);
		/*//로깅 파일 저장 확인용*/
		 logger.info("BU_RU_RUNG 실행 시작");
	}
}
