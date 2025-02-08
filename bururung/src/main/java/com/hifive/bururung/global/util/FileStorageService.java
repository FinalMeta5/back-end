package com.hifive.bururung.global.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {
	private final String UPLOAD_DIR = "C:/uploads/";
	
	public String saveFile (MultipartFile file, String subDir) {
		if (file.isEmpty()) {
			throw new IllegalArgumentException("파일이 비어있습니다.");
		}
		
		try {
			//업로드할 폴더 생성 (없으면 자동 생성)
			String saveDir = UPLOAD_DIR + subDir;
			Files.createDirectories(Paths.get(saveDir));
			
			// 파일명 랜덥 UUID 생성 (파일 확장자 유지)
			String originalFileName = file.getOriginalFilename();
			String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
			String savedFileName = UUID.randomUUID().toString() + fileExtension;
			
			//파일 저장
			File destFile = new File(saveDir, savedFileName);
			file.transferTo(destFile);
			
			return savedFileName; // DB 저장 파일명 반환
		} catch (IOException e) {
			throw new RuntimeException("파일 저장 중 오류 발생 : "+ e.getMessage());
		}
	}
}
