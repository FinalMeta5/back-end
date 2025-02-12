package com.hifive.bururung.domain.carshare.participant.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AllCarListResponse {
	private Long carShareRegiId;
	private Long carId;
	private Long memberId;
	private Long latitudePl;
	private Long longitudePl;
	private Long passengersNum;
	private Long latitudeDs;
	private Long longitudeDs;
	private String pickupDate;
	private String category;
}
