package com.hifive.bururung.domain.member.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface IMypageService {
	String uploadProfile(MultipartFile multipartFile, String subpath, Long memberId) throws IOException;
}
