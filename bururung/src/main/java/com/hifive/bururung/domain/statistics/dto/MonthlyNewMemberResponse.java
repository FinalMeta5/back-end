package com.hifive.bururung.domain.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyNewMemberResponse {
	private String month;
	private Integer count;
}
