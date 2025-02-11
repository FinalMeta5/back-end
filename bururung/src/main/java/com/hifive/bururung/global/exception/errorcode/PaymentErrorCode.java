package com.hifive.bururung.global.exception.errorcode;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentErrorCode implements ErrorCode {
	PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 결제를 찾을 수 없습니다"),
	PAYMENT_AMOUNT_ERROR(HttpStatus.BAD_REQUEST, "결제 금액이 다릅니다"),
	CREDIT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 상품을 찾을 수 없습니다");
    
	private final HttpStatus status;
	private final String message;
}
