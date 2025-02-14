package com.hifive.bururung.domain.admin.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminPaymentDTO {
	private Long paymentId;
	private Timestamp createdDate;
	private Timestamp approvedDate;
	private String method;
	private String orderId;
	private Long price;
	private Long creditId;
	private Long memberId;
}
