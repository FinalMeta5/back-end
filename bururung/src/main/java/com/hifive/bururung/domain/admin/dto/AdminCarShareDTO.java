package com.hifive.bururung.domain.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminCarShareDTO {
	private Long carShareRegiId;
	private Integer joinCount;
	private Long carId;
	private Long memberId;
	private Integer passengersNum;
	private String pickupLoc;
	private String pickupDate;
	private String destination;
	private String category;
	private String createdDate;
}
