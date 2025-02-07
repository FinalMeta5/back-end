package com.hifive.bururung.domain.carshare.participant.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CarInformationResponse {
	private String carModel;
	private String carNumber;
	private String color;
	private String carDescription;
}
