package com.hifive.bururung.domain.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminPaymentResponse<T> {
	private boolean success;
	private T data;
	private String message;
}
