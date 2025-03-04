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
import com.hifive.bururung.domain.carshare.participant.dto.PastParticipationListResponse;

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
	
	// 10. 과거 차량 탑승 내역 조회
	List<PastParticipationListResponse> findPastParticipationList(Long userId);
	
	// 11. 오늘 차량 탑승 내역 조회
	List<PastParticipationListResponse> findTodayParticipationList(Long userId);
	
	// 12. 탑승 여부 탄다로 변경
	int updateStateOK(Long carShareJoinId);
	
	// 13. 탑승 여부 안탄다로 변경
	int updateStateNO(Long carShareJoinId);
	
	// 14. 카테고리 별 공유차량 목록 조회
	List<AllCarListResponse> findByCategoryShareCarList(String category);
	
	// 15. 차량 예약 인원 조회
	int findJoinCountByCarShareRegiId(Long carShareRegiId);
}
