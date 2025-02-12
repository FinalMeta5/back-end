package com.hifive.bururung.domain.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminCarRegistrationRequest {
	private Long carId;
	private Long memberId;
}
