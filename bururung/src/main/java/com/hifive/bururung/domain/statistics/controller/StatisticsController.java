package com.hifive.bururung.domain.statistics.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hifive.bururung.domain.statistics.dto.AgeGroupRatioResponse;
import com.hifive.bururung.domain.statistics.dto.GenderRatioResponse;
import com.hifive.bururung.domain.statistics.dto.MemberNameWithRateResponse;
import com.hifive.bururung.domain.statistics.dto.MonthlyNewMemberResponse;
import com.hifive.bururung.domain.statistics.service.IStatisticsService;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {
	
	@Autowired
	private IStatisticsService iStatisticsService;
	
	@GetMapping("/gender-ratio")
	public List<GenderRatioResponse> getGenderRatio() {
		return iStatisticsService.getGenderRatio();
	}
	
	@GetMapping("/age-ratio")
	public List<AgeGroupRatioResponse> getAgeGroupRatio() {
		return iStatisticsService.getAgeGroupRatio();
	}
	
	@GetMapping("/monthly-member")
	public List<MonthlyNewMemberResponse> getMonthlyNewMemberCount() {
		return iStatisticsService.getMonthlyNewMemberCount();
	}
	
	@GetMapping("/member-rate")
	public List<MemberNameWithRateResponse> getMembersByRatingDesc() {
		return iStatisticsService.getMembersByRatingDesc();
	}
}
