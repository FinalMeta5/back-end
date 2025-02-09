package com.hifive.bururung.domain.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenderRatioResponse {
	private String gender;
	private Integer count;
}
