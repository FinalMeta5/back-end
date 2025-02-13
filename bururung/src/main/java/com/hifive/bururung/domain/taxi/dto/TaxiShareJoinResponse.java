package com.hifive.bururung.domain.taxi.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaxiShareJoinResponse {
	private Long tsjId;
	private Long taxiShareId;
	private Long memberId;
	private Date joinTime;
	private String pickupLocation;
	private Date pickupDate;
	private String pickupTime;
	private int count;
}
