package com.hifive.bururung.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentConfirmResponse {
	
	private String paymentKey;
	
	private String orderId;
	
	private String method;
	
	private String totalAmount;
	
	private String status;
	
}
