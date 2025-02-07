package com.hifive.bururung.domain.carshare.participant.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DrivingInformation {
	private Double latitudePl;
	private Double longitudePl;
	private Double latitudeDs;
	private Double longitudeDs;
}
