package com.hifive.bururung.domain.statistics.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hifive.bururung.domain.statistics.dto.AgeGroupRatioResponse;
import com.hifive.bururung.domain.statistics.dto.GenderRatioResponse;
import com.hifive.bururung.domain.statistics.dto.MonthlyNewMemberResponse;
import com.hifive.bururung.domain.statistics.repository.IStatisticsMapper;

@Service
public class StatisticsService implements IStatisticsService {
	
	@Autowired
	private IStatisticsMapper iStatisticsMapper;

	@Override
	@Transactional
	public List<GenderRatioResponse> getGenderRatio() {
		List<GenderRatioResponse> genderCountList = iStatisticsMapper.getGenderCount();
		int totalCount = genderCountList.stream().mapToInt(GenderRatioResponse::getCount).sum();
		List<GenderRatioResponse> genderRatioList = genderCountList.stream()
	            .map(rawData -> {
	            	GenderRatioResponse transformedData = new GenderRatioResponse();
	            	transformedData.setCount(totalCount > 0 ? rawData.getCount() * 100 / totalCount : 0);
	            	transformedData.setGender("M".equals(rawData.getGender()) ? "남자" : "여자");
	            	return transformedData;
	            })
	            .collect(Collectors.toList());
		return genderRatioList;
	}

	@Override
	public List<AgeGroupRatioResponse> getAgeGroupRatio() {
		return iStatisticsMapper.getAgeGroupRatio();
	}
	
	@Override
	public List<MonthlyNewMemberResponse> getMonthlyNewMemberCount() {
		return iStatisticsMapper.getMonthlyNewMemberCount();
	}
}
