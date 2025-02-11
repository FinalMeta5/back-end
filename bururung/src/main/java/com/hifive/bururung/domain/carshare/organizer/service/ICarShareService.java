package com.hifive.bururung.domain.carshare.organizer.service;

import java.time.LocalDateTime;
import java.util.List;

import com.hifive.bururung.domain.carshare.organizer.dto.CarShareRegiRequestDTO;
import com.hifive.bururung.domain.carshare.organizer.entity.CarShareRegistration;

public interface ICarShareService{
	// 차량 공유 서비스 등록
	CarShareRegistration registerCarShare(CarShareRegiRequestDTO request, Long memberId, Long carId, LocalDateTime pickupDateTime);
	
	// 내가 등록한 차량 공유 서비스 전체 리스트 조회
	List<CarShareRegistration> getAllCarSharesByMemberId(Long memberId);
}
