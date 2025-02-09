package com.hifive.bururung.domain.statistics.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.hifive.bururung.domain.statistics.dto.AgeGroupRatioResponse;
import com.hifive.bururung.domain.statistics.dto.GenderRatioResponse;

@Repository
@Mapper
public interface IStatisticsMapper {

	List<GenderRatioResponse> getGenderCount();

	List<AgeGroupRatioResponse> getAgeGroupRatio();
}
