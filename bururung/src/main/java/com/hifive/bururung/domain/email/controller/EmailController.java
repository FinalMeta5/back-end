package com.hifive.bururung.domain.email.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hifive.bururung.domain.email.dto.EmailCheckRequest;
import com.hifive.bururung.domain.email.dto.EmailSendRequest;
import com.hifive.bururung.domain.email.dto.EmailType;
import com.hifive.bururung.domain.email.service.EmailService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/email")
public class EmailController {
	
	private final EmailService emailService;
	
	@PostMapping("/signup")
	public ResponseEntity sendSignupMail(@RequestBody EmailSendRequest emailSendRequest) {
		String code = emailService.createCode(emailSendRequest.getEmail());
		emailService.sendAuthenticationMail(EmailType.SIGNUP, emailSendRequest.getEmail(), code);
		
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/change-password")
	public ResponseEntity sendChangePasswordMail(HttpSession session, @RequestBody EmailSendRequest emailSendRequest) {
		String code = emailService.createCode(emailSendRequest.getEmail());
		emailService.sendAuthenticationMail(EmailType.CHANGE_PASSWORD, emailSendRequest.getEmail(), code);
		
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/check")
	public ResponseEntity<Boolean> checkSignupAuthentication(HttpSession session, @RequestBody EmailCheckRequest emailCheckRequest) {
		return ResponseEntity.ok(emailService.isAuthenticated(emailCheckRequest.getEmail(), emailCheckRequest.getCode()));
	}
}
