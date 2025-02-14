package com.hifive.bururung.domain.member.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hifive.bururung.domain.member.dto.CreditHistoryResponse;
import com.hifive.bururung.domain.member.dto.MypageResponse;
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
	
	@GetMapping("/credit-history")
	public ResponseEntity<List<CreditHistoryResponse>> getCreditHistory(@AuthenticationPrincipal User user, @RequestParam("year") int year, @RequestParam("month") int month) {
		Long memberId = Long.parseLong(user.getUsername());
		return ResponseEntity.ok(mypageService.getCreditHistory(memberId, year, month));
	}
	
	@GetMapping("/credit/{memberId}")
	public ResponseEntity<Integer> getCreditBalance(@PathVariable("memberId") Long memberId) {
		return ResponseEntity.ok(mypageService.getCreditBalance(memberId));
	}
	
	@GetMapping
	public ResponseEntity<MypageResponse> getMypage(@AuthenticationPrincipal User user) {
		Long memberId = Long.parseLong(user.getUsername());
		
		return ResponseEntity.ok(mypageService.getMypage(memberId));
	}
}
