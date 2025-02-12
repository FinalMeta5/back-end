package com.hifive.bururung.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MypageResponse {
	private Integer carRegistrationCount;
	
	private Integer carJoinCount;
	
	private Integer taxiJoinCount;
	
	private String imageUrl;
}
