package com.hifive.bururung.domain.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminTaxiShareDTO {
	private Long taxiShareId;
	private Integer joinCount;
	private Long memberId;
	private Integer passengersNum;
	private String pickupLocation;
	private String pickupTime;
	private String destination;
	private String createdDate;
}
