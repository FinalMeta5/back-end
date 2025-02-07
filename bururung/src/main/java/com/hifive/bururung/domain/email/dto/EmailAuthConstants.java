package com.hifive.bururung.domain.email.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmailAuthConstants {
	EMAIL("EMAIL"),
	EMAIL_AUTH("EMAIL_AUTH"),
	EMAIL_AUTH_CODE("EMAIL_AUTH_CODE"),
	EMAIL_AUTH_LIMIT_SEC("300");
	
	private final String value;
}
