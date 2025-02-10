package com.hifive.bururung.domain.carshare.participant.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DrivingInformationRequest {
	private Long memberId;
	private Long carShareRegiId;
}
