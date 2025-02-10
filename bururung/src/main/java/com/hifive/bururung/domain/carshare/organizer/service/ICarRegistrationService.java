package com.hifive.bururung.domain.carshare.organizer.service;

import java.util.List;
import java.util.Optional;

import com.hifive.bururung.domain.carshare.organizer.entity.CarRegistration;

public interface ICarRegistrationService {
	// create
	CarRegistration registerCar(CarRegistration car); // 차량 등록
	
	// read
	List<CarRegistration> getAllCars(); // 전체 차량 리스트 조회
	Optional<CarRegistration> getCarByCarId(Long carId); // 특정 차량 조회
	Optional<CarRegistration> getCarByCarNumber(String carNumber); // 특정 차량 번호로 차량 조회
	Optional<CarRegistration> getCarByMemberId(Long memberId); // 특정 회원 차량 조회
	
	// update
	CarRegistration updateCar(CarRegistration car);// 차량 정보 수정
	
	// delete
	void deleteCar(Long carId); // 차량 삭제

	boolean isCarAlreadyRegistered(Long memberId);
	
	boolean isVerified(Long memberId);

	boolean isCarNumberExists(String carNumber);
}