package com.hifive.bururung.domain.carshare.organizer.controller;



import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hifive.bururung.domain.carshare.organizer.dto.CarShareRegiRequestDTO;
import com.hifive.bururung.domain.carshare.organizer.dto.MyCarServiceParticipantListResponseDTO;
import com.hifive.bururung.domain.carshare.organizer.entity.CarShareRegistration;
import com.hifive.bururung.domain.carshare.organizer.repository.CarRegistrationRepository;
import com.hifive.bururung.domain.carshare.organizer.service.ICarShareService;

@RestController
@RequestMapping("/api/car-share")
public class CarShareController {
	
	private final ICarShareService carShareService;
	private final CarRegistrationRepository carRegistrationRepository;
	
	public CarShareController(ICarShareService carShareService, CarRegistrationRepository carRegistrationRepository) {
		this.carShareService = carShareService;
		this.carRegistrationRepository = carRegistrationRepository;

	}

    @PostMapping("/register")
    public ResponseEntity<String> registerCarShare(@RequestBody CarShareRegiRequestDTO request, Authentication authentication) {
        if (request == null) {
            System.out.println("❌ 요청 데이터가 null 입니다.");
            return ResponseEntity.badRequest().body("요청 데이터가 없습니다.");
        }

        // 현재 로그인한 사용자 ID 가져오기
        Long memberId = Long.parseLong(authentication.getName());

        // 차량 ID 조회
        Long carId = carRegistrationRepository.findCarIdByMemberId(memberId);
        if (carId == null) {
            return ResponseEntity.badRequest().body("🚨 해당 회원의 차량 정보가 없습니다.");
        }

        // pickupDate 변환 (프론트에서 "2025-02-14T06:00" 형식으로 보냄)
        LocalDateTime pickupDateTime = LocalDateTime.parse(request.getPickupDate(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        // DTO → 엔티티 변환 후 저장
        CarShareRegistration savedCarShare = carShareService.registerCarShare(request, memberId, carId, pickupDateTime);

        return ResponseEntity.ok("차량 공유 서비스 등록 성공 (ID: " + savedCarShare.getCarShareRegiId() + ")");
    }
    
    @GetMapping("/my-list")
    public ResponseEntity<List<CarShareRegistration>> getMyCarShares(Authentication authentication) {
        Long memberId = Long.parseLong(authentication.getName());

        List<CarShareRegistration> myCarShares = carShareService.getAllCarSharesByMemberId(memberId);

        if (myCarShares.isEmpty()) {
            return ResponseEntity.noContent().build(); // 데이터가 없으면 `204 No Content` 반환
        }

        return ResponseEntity.ok(myCarShares);
    }

    @GetMapping("/participants/{carShareRegiId}")
    public ResponseEntity<List<MyCarServiceParticipantListResponseDTO>> getMyCarServiceParticipants(
        @PathVariable("carShareRegiId") Long carShareRegiId
    ){
        System.out.println("🔍 요청된 carShareRegiId: " + carShareRegiId); // 로그 확인
        List<MyCarServiceParticipantListResponseDTO> participants = carShareService.getMyCarShareServiceParticipantList(carShareRegiId);
        System.out.println(participants);
        return ResponseEntity.ok(participants);
    }

    
    @PostMapping("/delete/{carShareRegiId}")
    public ResponseEntity<String> deleteCarShare(@PathVariable("carShareRegiId") Long carShareRegiId, Authentication authentication){
    	Long memberId = Long.parseLong(authentication.getName());
    	Optional<CarShareRegistration> carShareOpt = carShareService.findById(carShareRegiId);
        CarShareRegistration carShare = carShareOpt.get();
        if (!carShare.getMemberId().equals(memberId)) {
            return ResponseEntity.status(403).body("🚨 삭제 권한이 없습니다.");
        }

        carShareService.deleteCarShare(carShareRegiId);
        return ResponseEntity.ok("✅ 차량 공유 서비스가 삭제되었습니다.");
    }
    
    


}
