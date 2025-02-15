package com.hifive.bururung.domain.carshare.participant.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PastParticipationListResponse {
	private Long carShareRegiId;
	private Long memberId;
	private String pickupDate;
	private String pickupLoc;
	private String destination;
	private String state;
}
