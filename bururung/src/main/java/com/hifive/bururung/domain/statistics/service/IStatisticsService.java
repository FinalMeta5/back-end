package com.hifive.bururung.domain.statistics.service;

import java.util.List;

import com.hifive.bururung.domain.statistics.dto.AgeGroupRatioResponse;
import com.hifive.bururung.domain.statistics.dto.GenderRatioResponse;
import com.hifive.bururung.domain.statistics.dto.MemberNameWithRateResponse;
import com.hifive.bururung.domain.statistics.dto.MonthlyNewMemberResponse;

public interface IStatisticsService {
	
	List<GenderRatioResponse> getGenderRatio();
	
	List<AgeGroupRatioResponse> getAgeGroupRatio();
	
	List<MonthlyNewMemberResponse> getMonthlyNewMemberCount();
	
	List<MemberNameWithRateResponse> getMembersByRatingDesc();
}
