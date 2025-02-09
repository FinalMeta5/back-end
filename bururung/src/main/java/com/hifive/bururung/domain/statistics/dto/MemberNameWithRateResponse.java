package com.hifive.bururung.domain.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberNameWithRateResponse {
	private String name;
	private Double avgRate;
}
