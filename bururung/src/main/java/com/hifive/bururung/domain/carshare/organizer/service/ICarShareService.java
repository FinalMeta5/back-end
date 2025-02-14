package com.hifive.bururung.domain.carshare.organizer.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.hifive.bururung.domain.carshare.organizer.dto.CarShareRegiRequestDTO;
import com.hifive.bururung.domain.carshare.organizer.dto.MyCarServiceParticipantListResponseDTO;
import com.hifive.bururung.domain.carshare.organizer.entity.CarShareRegistration;

public interface ICarShareService{
	// 차량 공유 서비스 등록
	CarShareRegistration registerCarShare(CarShareRegiRequestDTO request, Long memberId, Long carId, LocalDateTime pickupDateTime);
	
	// 내가 등록한 차량 공유 서비스 전체 리스트 조회
	List<CarShareRegistration> getAllCarSharesByMemberId(Long memberId);

	// 차량 서비스 삭제 : 참여자 없을 때 만 가능함.
	void deleteCarShare(Long carShareRegiId);

	// 차량 공유 서비스 아이디로 찾기
	Optional<CarShareRegistration> findById(Long id);
	
	// 내 차량 공유 서비스 참가자 리스트 조회
	List<MyCarServiceParticipantListResponseDTO> getMyCarShareServiceParticipantList(Long carShareRegiId);
}
