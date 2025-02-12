package com.hifive.bururung.domain.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminPaymentDTO {
	private Long paymentId;
	private String createdDate;
	private String approvedDate;
	private String method;
	private String orderId;
	private Long price;
	private Long creditId;
	private Long memberId;
}
