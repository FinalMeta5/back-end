package com.hifive.bururung.domain.member.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hifive.bururung.domain.member.dto.ChangePasswordRequest;
import com.hifive.bururung.domain.member.dto.FindEmailRequest;
import com.hifive.bururung.domain.member.dto.LoginRequest;
import com.hifive.bururung.domain.member.dto.SignupRequest;
import com.hifive.bururung.domain.member.dto.TokenDTO;
import com.hifive.bururung.domain.member.service.IMemberService;
import com.hifive.bururung.global.common.CookieUtil;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
	
	private final IMemberService memberService;
	private final CookieUtil cookieUtil;
	
	@Value("${jwt.refresh-token-validity}")
	private Long refreshTokenValidity;
	
	@PostMapping("/login")
	public ResponseEntity<String> login(HttpServletResponse response, @RequestBody LoginRequest loginRequest) {
		TokenDTO token = memberService.login(loginRequest.getEmail(), loginRequest.getPassword());
		
		setAccessToken(response, token.getAccessToken());
		setRefreshToken(response, token.getRefreshToken());
		
		return ResponseEntity.ok(token.getEmail());
	}
	
	@PostMapping("/signup")
	public ResponseEntity<Long> signup(@RequestBody SignupRequest signupRequest) {
		Long memberId = memberService.signup(signupRequest);
		
		return ResponseEntity.ok(memberId);
	}
	
	@GetMapping("/find-email")
	public ResponseEntity<String> findId(@RequestBody FindEmailRequest findEmailRequest) {
		return ResponseEntity.ok(memberService.findEmail(findEmailRequest.getName(), findEmailRequest.getPhone()));
	}
	
	@PostMapping("/change-password")
	public ResponseEntity<Long> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
		return ResponseEntity.ok(memberService.changePassword(changePasswordRequest.getEmail(), changePasswordRequest.getPassword()));
	}
	
	private void setAccessToken(HttpServletResponse response, String accessToken) {
		response.setHeader("accesstoken", accessToken);
	}
	
	private void setRefreshToken(HttpServletResponse response, String refreshToken) {
		cookieUtil.addCookie(response, "refreshtoken", refreshToken, refreshTokenValidity.intValue());
	}
}
