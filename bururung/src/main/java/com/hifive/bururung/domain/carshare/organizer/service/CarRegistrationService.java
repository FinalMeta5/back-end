package com.hifive.bururung.domain.carshare.organizer.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hifive.bururung.domain.carshare.organizer.entity.CarRegistration;
import com.hifive.bururung.domain.carshare.organizer.repository.CarRegistrationRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CarRegistrationService implements ICarRegistrationService {
	private final CarRegistrationRepository carRegistrationRepository;
	
	public CarRegistrationService(CarRegistrationRepository carRegistrationRepository) {
		this.carRegistrationRepository = carRegistrationRepository;
	}
	
	
	public boolean isCarAlreadyRegistered(Long memberId) {
		return carRegistrationRepository.existsByMember_MemberId(memberId);
	}
	
	@Override
	public boolean isVerified(Long memberId) {
	    String verified = carRegistrationRepository.findVerifiedByMemberId(memberId);
	    return "Y".equals(verified); // 'Y'면 true, 그 외는 false 반환
	}
	
	@Override
	public CarRegistration registerCar(CarRegistration car) {
		return carRegistrationRepository.save(car);
	}

	@Override
	public List<CarRegistration> getAllCars() {
		return carRegistrationRepository.findAll();
	}

	@Override
	public Optional<CarRegistration> getCarByCarId(Long carId) {
		return carRegistrationRepository.findById(carId);
	}

	@Override
	public Optional<CarRegistration> getCarByCarNumber(String carNumber) {
		return carRegistrationRepository.findByCarNumber(carNumber);
	}

	@Override
	public Optional<CarRegistration> getCarByMemberId(Long memberId) {
		return carRegistrationRepository.findByMember_MemberId(memberId);
	}

	@Override
	@Transactional
	public CarRegistration updateCar(CarRegistration car) {
		 return carRegistrationRepository.save(car);
	}

	@Override
	public void deleteCar(Long carId) {
		if (carRegistrationRepository.existsById(carId)) {
			carRegistrationRepository.deleteById(carId);
		} else {
			throw new RuntimeException("해당 차량이 존재하지 않습니다.");
		}
	}


	@Override
	@Transactional
	public boolean isCarNumberExists(String carNumber) {
	    Long count = carRegistrationRepository.countByCarNumber(carNumber);
	    return count != null && count > 0;
	}





}
