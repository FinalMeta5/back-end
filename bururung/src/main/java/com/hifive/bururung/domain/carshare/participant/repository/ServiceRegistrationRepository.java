package com.hifive.bururung.domain.carshare.participant.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.hifive.bururung.domain.carshare.participant.dto.AllCarListResponse;
import com.hifive.bururung.domain.carshare.participant.dto.AvailableCarShareListResponse;
import com.hifive.bururung.domain.carshare.participant.dto.CarInformationResponse;
import com.hifive.bururung.domain.carshare.participant.dto.DriverInformationResponse;
import com.hifive.bururung.domain.carshare.participant.dto.DrivingInformationResponse;

@Mapper
@Repository
public interface ServiceRegistrationRepository {
	// 1. 현재 탑승 가능한 공유 차량 목록 출력
	List<AvailableCarShareListResponse> findAvailableCarShareList();
	
	// 2. 운전자 정보
	DriverInformationResponse findDriverInformation(Long memberId);
	
	// 3. 차량 정보
	CarInformationResponse findCarInformation(Long memberId);
	
	// 4. 차량 운행 정보
	DrivingInformationResponse findDrivingInformation(Map<String, Object> params);
	
	// 5. 차량 공유 예약
	boolean insertRegistration(Map<String, Object> params);
	
	// 6. 리뷰 평점 조회
	Double findRating(Long memberId);

	// 7. 잔여 크레딧 조회
	int findLeftoverCredit(Long userId);
	
	// 8. 크레딧 차감
	void insertCreditByCar(Long userId);
	
	// 9. 전체 공유 차량 목록 조회
	List<AllCarListResponse> findAllShareCarList();
}
