package com.hifive.bururung.domain.carshare.organizer.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hifive.bururung.domain.carshare.organizer.dto.CarShareRegiRequestDTO;
import com.hifive.bururung.domain.carshare.organizer.dto.MyCarServiceParticipantListResponseDTO;
import com.hifive.bururung.domain.carshare.organizer.entity.CarShareRegistration;
import com.hifive.bururung.domain.carshare.organizer.repository.CarShareMapper;
import com.hifive.bururung.domain.carshare.organizer.repository.CarShareRepository;

import jakarta.transaction.Transactional;

@Service
public class CarShareService implements ICarShareService{
	private final CarShareRepository carShareRepository;
	private final CarShareMapper carShareMapper;
	
	public CarShareService (CarShareRepository carShareRepository, CarShareMapper carShareMapper) {
		this.carShareRepository = carShareRepository;
		this.carShareMapper = carShareMapper;
	}

	// 차량 공유 서비스 등록
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

    // 내 차량 공유 서비스 전체 조회
    @Override
    public List<CarShareRegistration> getAllCarSharesByMemberId(Long memberId) {
        return carShareRepository.findByMemberId(memberId);
    }
    
    
    // 차량 공유 서비스 아이디로 공유 정보 조회
    @Override
    public Optional<CarShareRegistration> findById(Long carShareRegiId) {
    	return carShareRepository.findById(carShareRegiId);
    }
    
    // 차량 공유 서비스 삭제
    @Transactional
    @Override
    public void deleteCarShare(Long carShareRegiId) {
    	carShareRepository.deleteById(carShareRegiId);
    }

    // 내 차량 서비스 참가자 리스트 조회
	@Override
	public List<MyCarServiceParticipantListResponseDTO> getMyCarShareServiceParticipantList(Long carShareRegiId) {
		return carShareMapper.findMyCarServiceParticipantList(carShareRegiId);
	}


}
