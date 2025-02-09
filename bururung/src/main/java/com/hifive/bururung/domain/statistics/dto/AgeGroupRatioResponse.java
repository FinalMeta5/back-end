package com.hifive.bururung.domain.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgeGroupRatioResponse {
	private String ageGroup;
	private Integer count;
}
