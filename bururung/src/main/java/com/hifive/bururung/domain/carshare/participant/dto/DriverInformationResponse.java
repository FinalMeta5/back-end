package com.hifive.bururung.domain.carshare.participant.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DriverInformationResponse {
	private String imageUrl;
	private Long age;
	private String gender;
	private String criminalStatus;
	private String nickname;
}
