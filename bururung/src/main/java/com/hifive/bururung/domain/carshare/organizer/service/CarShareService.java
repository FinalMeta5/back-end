package com.hifive.bururung.domain.carshare.organizer.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hifive.bururung.domain.carshare.organizer.entity.CarShareRegistration;
import com.hifive.bururung.domain.carshare.organizer.repository.CarShareRepository;

@Service
public class CarShareService implements ICarShareService{
	private final CarShareRepository carShareRepository;
	
	public CarShareService (CarShareRepository carShareRepository) {
		this.carShareRepository = carShareRepository;
	}

	@Override
	public CarShareRegistration registerCarShare(CarShareRegistration carShare) {
		return carShareRepository.save(carShare);
	}

	@Override
	public Optional<CarShareRegistration> getAllCarSharesByMemberId(Long memberId) {
		return carShareRepository.findById(memberId);
	}



}
