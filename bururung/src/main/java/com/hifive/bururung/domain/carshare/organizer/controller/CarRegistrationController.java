package com.hifive.bururung.domain.carshare.organizer.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hifive.bururung.domain.carshare.organizer.dto.CarUpdateRequestDTO;
import com.hifive.bururung.domain.carshare.organizer.dto.CarUpdateResponseDTO;
import com.hifive.bururung.domain.carshare.organizer.entity.CarRegistration;
import com.hifive.bururung.domain.carshare.organizer.service.ICarRegistrationService;
import com.hifive.bururung.domain.member.entity.Member;
import com.hifive.bururung.domain.member.service.MemberService;

@RestController
@RequestMapping("/api/car-registration")
public class CarRegistrationController {
	private final ICarRegistrationService carRegistrationService;
	private final MemberService  memberService;
	
	public CarRegistrationController(ICarRegistrationService carRegistrationService, MemberService memberService) {
		this.carRegistrationService = carRegistrationService;
		this.memberService = memberService;
	}
	
	// create : 차량 등록
	@PostMapping("/register")
	public ResponseEntity<String> registerCar(@AuthenticationPrincipal User user, @RequestBody CarRegistration car) {
	    String memberIdStr = user.getUsername();
	    Long sessionMemberId;

	    try {
	        sessionMemberId = Long.parseLong(memberIdStr);
	    } catch (NumberFormatException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 회원 ID 형식입니다.");
	    }

	    if (carRegistrationService.isCarAlreadyRegistered(sessionMemberId)) {
	        return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 등록된 차량이 있습니다.");
	    }

	    // ✅ 올바르게 Member 가져오기
	    Member member = memberService.findByMemberId(sessionMemberId);
	    car.setMember(member);
	    carRegistrationService.registerCar(car);

	    return ResponseEntity.ok("차량이 성공적으로 등록되었습니다.");
	}
	
	// read : 전체 조회, 차량번호로 조회, 회원아이디로 조회, 차량 아이디로 조회
	// 전체 등록된 차량 리스트 조회
	@GetMapping("/list")
	public ResponseEntity<List<CarRegistration>> getAllCars() {
		return ResponseEntity.ok(carRegistrationService.getAllCars());
	}
	
	// 차량 번호로 차량의 정보 조회
	@GetMapping("/car/by-number/{carNumber}")
	public  ResponseEntity<CarRegistration> getCarByCarNimber(@PathVariable("carNumber") String carNumber) {
		Optional<CarRegistration> car = carRegistrationService.getCarByCarNumber(carNumber);
		return car.map(ResponseEntity::ok)
				.orElseGet(()->ResponseEntity.notFound().build());
	}
	
	// 해당 차량아이디로 차량 등록 조회
	@GetMapping("/car/by-id/{carId}")
	public  ResponseEntity<CarRegistration> getCarByCarId(@PathVariable("carId") Long carId) {
		Optional<CarRegistration> car = carRegistrationService.getCarByCarId(carId);
		return car.map(ResponseEntity::ok)
				.orElseGet(()->ResponseEntity.notFound().build());
	}
	
	// 해당 회원아이디로 차량 등록 조회
	@GetMapping("/member/{memberId}")
	public  ResponseEntity<CarRegistration> getCarByMemberId(@PathVariable("memberId") Long memberId) {
		Optional<CarRegistration> car = carRegistrationService.getCarByMemberId(memberId);
		return car.map(ResponseEntity::ok)
				.orElseGet(()->ResponseEntity.notFound().build());
	}
	
	// update 
	// 차량 정보 수정 (로그인 된 유저가 본인의 정보 수정만 가능)
	@PutMapping("/update/{carId}")
	public ResponseEntity<?> updateCar(@PathVariable("carId") Long carId, 
			@AuthenticationPrincipal User user, 
			@RequestBody CarUpdateRequestDTO updateCarRequest){
	    String memberIdStr = user.getUsername();
	    Long sessionMemberId;
	    try {
	        sessionMemberId = Long.parseLong(memberIdStr);
	    } catch (NumberFormatException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 회원 ID 형식입니다.");
	    }

	    // 로그인한 사용자의 차량인지 확인
	    Optional<CarRegistration> carOptional = carRegistrationService.getCarByCarId(carId);
	    if (carOptional.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 차량을 찾을 수 없습니다.");
	    }

	    CarRegistration car = carOptional.get();
	    
	    // 차량 소유자 검증
	    if (!car.getMember().getMemberId().equals(sessionMemberId)) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("해당 차량을 수정할 권한이 없습니다.");
	    }

	    // 차량 정보 업데이트
	    car.setCarNumber(updateCarRequest.getCarNumber());
	    car.setCarModel(updateCarRequest.getCarModel());
	    car.setMaxPassengers(updateCarRequest.getMaxPassengers());
	    car.setColor(updateCarRequest.getColor());
	    car.setImageUrl(updateCarRequest.getImageUrl());
	    car.setImageName(updateCarRequest.getImageName());
	    car.setVerifiedFile(updateCarRequest.getVerifiedFile());
	    car.setCarDescription(updateCarRequest.getCarDescription());

	    CarRegistration updatedCar = carRegistrationService.updateCar(car);
	    return ResponseEntity.ok(new CarUpdateResponseDTO(updatedCar));
	}
	
	//delete
	// 차량 삭제
	@DeleteMapping("/delete/{carId}")
	public ResponseEntity<String> deleteCar(@PathVariable("carId") Long carId) {
		carRegistrationService.deleteCar(carId);
		return ResponseEntity.ok("차량이 성공적으로 삭제되었습니다.");
	}

}
