package com.hifive.bururung.domain.member.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hifive.bururung.domain.member.service.IMemberService;
import com.hifive.bururung.domain.member.service.IMypageService;
import com.hifive.bururung.global.common.s3.FileSubPath;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MypageController {
	
	private final IMypageService mypageService;

	
	@PostMapping("/upload-profile")
	public ResponseEntity<String> uploadProfile(@AuthenticationPrincipal User user, @RequestPart("profileImage") MultipartFile profileImage) throws IOException {
		Long memberId = Long.parseLong(user.getUsername());
		
		String url = mypageService.uploadProfile(profileImage, FileSubPath.MEMBER.getPath(), memberId);
		
		return ResponseEntity.ok(url);
	}
}
