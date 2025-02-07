package com.hifive.bururung.global.exception.errorcode;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberErrorCode implements ErrorCode {
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다"),
	EMAIL_AUTH_FAIL(HttpStatus.BAD_REQUEST, "이메일 인증 실패"),
	MALFORMED_TOKEN(HttpStatus.UNAUTHORIZED, "올바르지 않은 토큰"),
	EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰"),
	UNAUTHORIZIED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다"),
	MAIL_SEND_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "메일 전송이 실패했습니다");
	
	private final HttpStatus status;
	private final String message;
}
