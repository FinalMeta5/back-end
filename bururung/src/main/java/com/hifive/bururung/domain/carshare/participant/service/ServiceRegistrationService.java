package com.hifive.bururung.domain.carshare.participant.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hifive.bururung.domain.carshare.organizer.entity.CarRegistration;
import com.hifive.bururung.domain.carshare.organizer.repository.CarRegistrationRepository;
import com.hifive.bururung.domain.carshare.participant.dto.AllCarListResponse;
import com.hifive.bururung.domain.carshare.participant.dto.AvailableCarShareListResponse;
import com.hifive.bururung.domain.carshare.participant.dto.CarInformationResponse;
import com.hifive.bururung.domain.carshare.participant.dto.DriverInformationResponse;
import com.hifive.bururung.domain.carshare.participant.dto.DrivingInformationResponse;
import com.hifive.bururung.domain.carshare.participant.dto.PastParticipationListResponse;
import com.hifive.bururung.domain.carshare.participant.repository.ServiceRegistrationRepository;
import com.hifive.bururung.global.exception.CustomException;
import com.hifive.bururung.global.exception.errorcode.CarRegistrationErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ServiceRegistrationService implements IServiceRegistrationService{
	
	private final ServiceRegistrationRepository serviceRegistrationRepository;
	private final CarRegistrationRepository carRegistrationRepository;
	
	// 1. 현재 이용 가능한 공유 차량 목록
	@Override
	public List<AvailableCarShareListResponse> getAvailableCarShareList() {
		return serviceRegistrationRepository.findAvailableCarShareList();
	}

	// 2. 운전자 정보
	@Override
	public DriverInformationResponse getDriverInformation(Long memberId) {
		return serviceRegistrationRepository.findDriverInformation(memberId);
	}

	// 3. 차량 정보
	@Override
	public CarInformationResponse getCarInformation(Long memberId) {
		return serviceRegistrationRepository.findCarInformation(memberId);
	}

	// 4. 차량 운행 정보
	@Override
	public DrivingInformationResponse getDrivingInformation(Long memberId, Long carShareRegiId) {
		Map<String, Object> params = new HashMap<>();
        params.put("memberId", memberId);
        params.put("carShareRegiId", carShareRegiId);
        return serviceRegistrationRepository.findDrivingInformation(params);
	}

	// 5. 공유 차량 예약
		@Retryable(
				retryFor = {ObjectOptimisticLockingFailureException.class },
				maxAttempts = 3,
				backoff = @Backoff(delay = 100)
		)
		@Override
		@Transactional
		public boolean insertRegistration(Long carShareRegiId, Long userId) {
			CarRegistration carRegistration = carRegistrationRepository.findByIdWithLock(carShareRegiId)
			.orElseThrow(() -> new CustomException(CarRegistrationErrorCode.CAR_NOT_FOUND));
			
			long leftoverCredit = serviceRegistrationRepository.findLeftoverCredit(userId);
			int joinCount = serviceRegistrationRepository.findJoinCountByCarShareRegiId(carShareRegiId);
			
			if(joinCount >= carRegistration.getMaxPassengers()) {
				throw new CustomException(CarRegistrationErrorCode.FULL_CAPACITY);
			}
			
			if(leftoverCredit >= 7) {
				Map<String, Object> params = new HashMap<>();
	            params.put("carShareRegiId", carShareRegiId);
	            params.put("userId", userId);
	            
	            serviceRegistrationRepository.insertRegistration(params);
	            return true;
			} else {
				return false;
			}
		}
	
	// 6. 리뷰 평점 조회
	@Override
	public Double findRating(Long memberId) {
		return serviceRegistrationRepository.findRating(memberId);
	}

	// 7. 잔여 크레딧 조회
	@Override
	public int findLeftoverCredit(Long userId) {
		return serviceRegistrationRepository.findLeftoverCredit(userId);
	}

	// 8. 크레딧 차감
	@Override
	public void insertCreditByCar(Long userId) {
		serviceRegistrationRepository.insertCreditByCar(userId);
	}

	// 9. 전체 공유 차량 목록 조회
	@Override
	public List<AllCarListResponse> findAllShareCarList() {
		return serviceRegistrationRepository.findAllShareCarList();
	}
	
	// 10. 과거 차량 탑승 내역 조회
	@Override
	public List<PastParticipationListResponse> findPastParticipationList(Long userId) {
		return serviceRegistrationRepository.findPastParticipationList(userId);
	}

	// 11. 오늘 차량 탑승 내역 조회
	@Override
	public List<PastParticipationListResponse> findTodayParticipationList(Long userId) {
		List<PastParticipationListResponse> result = serviceRegistrationRepository.findTodayParticipationList(userId);
		return result != null ? result : Collections.emptyList();
	}

	// 12. 탑승 여부 탄다로 변경
	@Override
	public int updateStateOK(Long carShareJoinId) {
		return serviceRegistrationRepository.updateStateOK(carShareJoinId);
	}

	// 13. 탑승 여부 안탄다로 변경
	@Override
	public int updateStateNO(Long carShareJoinId) {
		return serviceRegistrationRepository.updateStateNO(carShareJoinId);
	}

	// 14. 카테고리 별 공유차량 목록 조회
	@Override
	public List<AllCarListResponse> findByCategoryShareCarList(String category) {
		return serviceRegistrationRepository.findByCategoryShareCarList(category);
	}
}
