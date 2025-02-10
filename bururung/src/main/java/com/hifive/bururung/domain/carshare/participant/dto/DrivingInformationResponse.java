package com.hifive.bururung.domain.carshare.participant.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DrivingInformationResponse {
	private Double latitudePl;
	private Double longitudePl;
	private Double latitudeDs;
	private Double longitudeDs;
	private String pickupDate;
	private Long passengersNum;
	private Long leftoverNum;
}
