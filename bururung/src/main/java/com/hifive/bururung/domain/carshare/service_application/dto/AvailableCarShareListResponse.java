package com.hifive.bururung.domain.carshare.service_application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AvailableCarShareListResponse {
	private Long carShareRegiId;
	private String nickname;
	private Double latitudePl;
	private Double longitudePl;
	private Double latitudeDs;
	private Double longitudeDs;
	private String pickupDate;
}
