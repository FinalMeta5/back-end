package com.hifive.bururung.domain.statistics.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hifive.bururung.domain.statistics.dto.GenderRatioResponse;
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
}
