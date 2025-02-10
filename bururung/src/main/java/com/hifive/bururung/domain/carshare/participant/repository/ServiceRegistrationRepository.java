package com.hifive.bururung.domain.carshare.participant.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.hifive.bururung.domain.carshare.participant.dto.AvailableCarShareListResponse;
import com.hifive.bururung.domain.carshare.participant.dto.CarInformationResponse;
import com.hifive.bururung.domain.carshare.participant.dto.DriverInformationResponse;

@Mapper
@Repository
public interface ServiceRegistrationRepository {
	// 1. 현재 탑승 가능한 공유 차량 목록 출력
	List<AvailableCarShareListResponse> findAvailableCarShareList();
	
	// 2. 운전자 정보
	DriverInformationResponse findDriverInformation(Long memberId);
	
	// 3. 차량 정보
	CarInformationResponse findCarInformation(Long memberId);

}
