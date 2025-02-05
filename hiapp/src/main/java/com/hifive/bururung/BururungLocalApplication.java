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
		 logger.trace("TRACE 로그 - 가장 상세한 디버깅 정보");
		 logger.debug("DEBUG 로그 - 디버깅에 유용한 정보");
		 logger.info("INFO 로그 - 애플리케이션 상태 정보"); logger.warn("WARN 로그 - 잠재적인 문제 경고");
		 logger.error("ERROR 로그 - 심각한 문제 발생");
	}
}
