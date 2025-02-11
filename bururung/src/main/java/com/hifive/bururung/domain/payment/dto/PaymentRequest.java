package com.hifive.bururung.domain.payment.dto;

import com.hifive.bururung.domain.payment.entity.PaymentMethod;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
	private String orderId;
	
	private Long amount;
	
	private Integer creditCount;
	
	private Long memberId;
	
	private PaymentMethod method;
}
