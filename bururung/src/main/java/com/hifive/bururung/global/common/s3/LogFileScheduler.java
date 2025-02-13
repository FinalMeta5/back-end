package com.hifive.bururung.global.common.s3;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class LogFileScheduler {

    private final S3Uploader s3Uploader;
    private final String logDir = "logs/";

    @Scheduled(cron = "0 51 14 * * ?")
    public void uploadLogsToS3() {
        File rootDir = new File(logDir);
        if (!rootDir.exists()) {
            log.warn("🚨 로그 디렉토리가 존재하지 않음: {}", logDir);
            return;
        }

        LocalDate today = LocalDate.now();
        String dateStr = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // ✅ logs/ 디렉토리 내의 모든 파일 중 aop-logs-YYYY-MM-DD-*.log 패턴만 업로드
        for (File logFile : Objects.requireNonNull(rootDir.listFiles())) {
            if (logFile.getName().startsWith("aop-logs-") && logFile.getName().contains(dateStr)) {
                try {
                    String s3Url = s3Uploader.uploadLogFile(logFile, "aop-logs");
                    log.info("✅ 로그 파일 업로드 완료 - S3 URL: {}", s3Url);
                } catch (Exception e) {
                    log.error("🚨 로그 파일 업로드 실패 - 파일명: {}", logFile.getName(), e);
                }
            }
        }
    }
}