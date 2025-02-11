package com.hifive.bururung.domain.carshare.organizer.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hifive.bururung.domain.carshare.organizer.dto.CarShareRegiRequestDTO;
import com.hifive.bururung.domain.carshare.organizer.entity.CarShareRegistration;
import com.hifive.bururung.domain.carshare.organizer.repository.CarShareRepository;

import jakarta.transaction.Transactional;

@Service
public class CarShareService implements ICarShareService{
	private final CarShareRepository carShareRepository;
	
	public CarShareService (CarShareRepository carShareRepository) {
		this.carShareRepository = carShareRepository;
	}

    @Transactional
    @Override
    public CarShareRegistration registerCarShare(CarShareRegiRequestDTO request, Long memberId, Long carId, LocalDateTime pickupDateTime) {
        // ✅ DTO → 엔티티 변환
        CarShareRegistration carShare = CarShareRegistration.builder()
                .memberId(memberId) // 로그인한 사용자 ID
                .carId(carId)
                .pickupLoc(request.getPickupLoc())
                .latitudePl(request.getLatitudePl())
                .longitudePl(request.getLongitudePl())
                .sidoPl(request.getSidoPl())
                .sigunguPl(request.getSigunguPl())
                .roadNamePl(request.getRoadnamePl())
                .destination(request.getDestination())
                .latitudeDs(request.getLatitudeDs())
                .longitudeDs(request.getLongitudeDs())
                .sidoDs(request.getSidoDs())
                .sigunguDs(request.getSigunguDs())
                .roadNameDs(request.getRoadnameDs())
                .passengersNum(request.getPassengersNum())
                .pickupDate(pickupDateTime)
                .category(request.getCategory())
                .build();

        // ✅ DB에 저장
        return carShareRepository.save(carShare);
    }

    @Override
    public List<CarShareRegistration> getAllCarSharesByMemberId(Long memberId) {
        return carShareRepository.findByMemberId(memberId);
    }


}
