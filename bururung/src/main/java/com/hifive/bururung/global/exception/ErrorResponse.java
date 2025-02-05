package com.hifive.bururung.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.hifive.bururung.global.exception.errorcode.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ErrorResponse {
	
	private final HttpStatus status;
	private final String code;
	private final String message;
	
	public ErrorResponse(ErrorCode errorCode) {
		this.status = errorCode.getStatus();
		this.code = errorCode.name();
		this.message = errorCode.getMessage();
	}
	
	public static ResponseEntity<ErrorResponse> of(CustomException e) {
		return ResponseEntity
				.status(e.getErrorCode().getStatus())
				.body(ErrorResponse.builder()
						.status(e.getErrorCode().getStatus())
						.code(e.getErrorCode().name())
						.message(e.getErrorCode().getMessage())
						.build());
	}
	
	public static ResponseEntity<ErrorResponse> of(ErrorCode errorCode) {
		return ResponseEntity
				.status(errorCode.getStatus())
				.body(ErrorResponse.builder()
						.status(errorCode.getStatus())
						.code(errorCode.name())
						.message(errorCode.getMessage())
						.build());
	}
}
