package com.hifive.bururung.global.exception;

import com.hifive.bururung.global.exception.errorcode.ErrorCode;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
	
	private final ErrorCode errorCode;
	
	public CustomException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
