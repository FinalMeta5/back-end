package com.hifive.bururung.domain.carshare.participant.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.hifive.bururung.domain.carshare.participant.dto.AvailableCarShareListResponse;

@Mapper
@Repository
public interface ServiceRegistrationRepository {
	// 1. 현재 탑승 가능한 공유 차량 목록 출력
	List<AvailableCarShareListResponse> findAvailableCarShareList();
}
