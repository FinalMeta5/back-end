package com.hifive.bururung.domain.statistics.service;

import java.util.List;

import com.hifive.bururung.domain.statistics.dto.GenderRatioResponse;

public interface IStatisticsService {
	
	List<GenderRatioResponse> getGenderRatio();
	
}
