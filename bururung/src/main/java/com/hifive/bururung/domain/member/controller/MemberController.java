package com.hifive.bururung.domain.member.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hifive.bururung.domain.member.dto.ChangePasswordRequest;
import com.hifive.bururung.domain.member.dto.CheckNicknameRequest;
import com.hifive.bururung.domain.member.dto.FindEmailRequest;
import com.hifive.bururung.domain.member.dto.LoginRequest;
import com.hifive.bururung.domain.member.dto.LoginResponse;
import com.hifive.bururung.domain.member.dto.SignupRequest;
import com.hifive.bururung.domain.member.dto.TokenDTO;
import com.hifive.bururung.domain.member.repository.MemberRepository;
import com.hifive.bururung.domain.member.service.IMemberService;
import com.hifive.bururung.global.common.CookieUtil;
import com.hifive.bururung.global.common.TokenProvider;
import com.hifive.bururung.global.exception.CustomException;
import com.hifive.bururung.global.exception.errorcode.MemberErrorCode;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
	
	private final IMemberService memberService;
	private final MemberRepository memberRepository;
	private final CookieUtil cookieUtil;
	private final TokenProvider tokenProvider;
	
	@Value("${jwt.refresh-token-validity}")
	private Long refreshTokenValidity;
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(HttpServletResponse response, @RequestBody LoginRequest loginRequest) {
		TokenDTO token = memberService.login(loginRequest.getEmail(), loginRequest.getPassword());
		
		setAccessToken(response, token.getAccessToken());
		setRefreshToken(response, token.getRefreshToken());
		
		return ResponseEntity.ok(memberService.getLoginResponse(loginRequest.getEmail()));
	}
	
	@PostMapping("/signup")
	public ResponseEntity<Long> signup(@RequestBody SignupRequest signupRequest) {
		Long memberId = memberService.signup(signupRequest);
		
		return ResponseEntity.ok(memberId);
	}
	
	@PostMapping("/logout")
	public ResponseEntity<String> logout(@AuthenticationPrincipal User user) {
		Long memberId = Long.parseLong(user.getUsername());
		
		memberService.logout(memberId);
		
		return ResponseEntity.ok("로그아웃 성공");
	}
	
	@PostMapping("/reissue")
	public ResponseEntity<String> reissue(HttpServletRequest request, HttpServletResponse response) {
		String refreshToken = getRefreshToken(request);
		if(refreshToken == null) {
			throw new CustomException(MemberErrorCode.NO_TOKEN);
		}
		
		Authentication authentication = tokenProvider.getAuthentication(refreshToken);
		String accessToken = memberService.reissue(authentication, refreshToken);
		
		setAccessToken(response, accessToken);
		
		return ResponseEntity.ok("재발급 성공");
	}
	
	@DeleteMapping
	public ResponseEntity<String> delete(@AuthenticationPrincipal User user, String password) {
		Long memberId = Long.parseLong(user.getUsername());
		memberService.delete(memberId, password);
		
		return ResponseEntity.ok("탈퇴 성공");
	}
	
	
	@PostMapping("/find-email")
	public ResponseEntity<String> findId(@RequestBody FindEmailRequest findEmailRequest) {
		return ResponseEntity.ok(memberService.findEmail(findEmailRequest.getName(), findEmailRequest.getPhone()));
	}
	
	@PostMapping("/change-password")
	public ResponseEntity<Long> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
		return ResponseEntity.ok(memberService.changePassword(changePasswordRequest.getEmail(), changePasswordRequest.getPassword()));
	}
	
	@PostMapping("/check-nickname")
	public ResponseEntity<Boolean> checkNicknameDuplicated(@RequestBody CheckNicknameRequest checkNicknameRequest) {
		return ResponseEntity.ok(memberService.checkNicknameDuplicated(checkNicknameRequest.getNickname()));
	}
	
	private void setAccessToken(HttpServletResponse response, String accessToken) {
		response.setHeader("accesstoken", accessToken);
	}
	
	private void setRefreshToken(HttpServletResponse response, String refreshToken) {
		cookieUtil.addCookie(response, "refreshtoken", refreshToken, refreshTokenValidity.intValue());
	}
	
	private String getRefreshToken(HttpServletRequest request) {
	    return cookieUtil.getCookie(request, "refreshtoken")
                .map(Cookie::getValue)
                .orElse(null);
	}
}
