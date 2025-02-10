package com.hifive.bururung.domain.carshare.participant.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hifive.bururung.domain.carshare.participant.dto.AvailableCarShareListResponse;
import com.hifive.bururung.domain.carshare.participant.dto.CarInformationResponse;
import com.hifive.bururung.domain.carshare.participant.dto.CarShareRegistrationRequest;
import com.hifive.bururung.domain.carshare.participant.dto.DriverInformationResponse;
import com.hifive.bururung.domain.carshare.participant.dto.DrivingInformationResponse;
import com.hifive.bururung.domain.carshare.participant.repository.ServiceRegistrationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ServiceRegistrationService implements IServiceRegistrationService{
	
	private final ServiceRegistrationRepository serviceRegistrationRepository;
	
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
	@Override
	public void insertRegistration(CarShareRegistrationRequest request) {
		serviceRegistrationRepository.insertRegistration(request);
	}
}
