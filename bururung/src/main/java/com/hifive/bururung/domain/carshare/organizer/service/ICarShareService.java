package com.hifive.bururung.domain.carshare.organizer.service;

import java.util.Optional;

import com.hifive.bururung.domain.carshare.organizer.entity.CarShareRegistration;

public interface ICarShareService{
	// 차량 공유 서비스 등록
	CarShareRegistration registerCarShare(CarShareRegistration carShare);
	
	// 내가 등록한 차량 공유 서비스 전체 리스트 조회
	Optional<CarShareRegistration> getAllCarSharesByMemberId(Long memberId);
}
