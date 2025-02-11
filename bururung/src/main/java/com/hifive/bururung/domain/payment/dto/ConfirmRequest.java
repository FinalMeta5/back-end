package com.hifive.bururung.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmRequest {
	private String orderId;
	
	private Long price;
	
	private String paymentKey;
}
