package com.hifive.bururung.domain.carshare.organizer.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hifive.bururung.domain.carshare.organizer.dto.CarRegistrationResponseDTO;
import com.hifive.bururung.domain.carshare.organizer.dto.CarUpdateRequestDTO;
import com.hifive.bururung.domain.carshare.organizer.dto.CarUpdateResponseDTO;
import com.hifive.bururung.domain.carshare.organizer.entity.CarRegistration;
import com.hifive.bururung.domain.carshare.organizer.service.ICarRegistrationService;
import com.hifive.bururung.domain.member.entity.Member;
import com.hifive.bururung.domain.member.service.MemberService;
import com.hifive.bururung.global.exception.CustomException;
import com.hifive.bururung.global.exception.errorcode.CarRegistrationErrorCode;
import com.hifive.bururung.global.util.FileStorageService;
import com.hifive.bururung.global.util.SecurityUtil;

@RestController
@RequestMapping("/api/car-registration")
public class CarRegistrationController {
	private final ICarRegistrationService carRegistrationService;
	private final MemberService  memberService;
	private final FileStorageService fileStorageService;
	
	public CarRegistrationController(ICarRegistrationService carRegistrationService, 
			MemberService memberService, FileStorageService fileStorageService) {
		this.carRegistrationService = carRegistrationService;
		this.memberService = memberService;
		this.fileStorageService = fileStorageService;
	}
	
	// 공백 제거 유틸 메서드
	private String cleanCarNumber(String carNumber) {
	    if (carNumber == null) return null;
	    return carNumber.trim().replaceAll("\\s+", ""); // 모든 공백 제거
	}
	
	// create : 차량 등록
	@PostMapping("/register")
	public ResponseEntity<String> registerCar(
	    @RequestParam("carImage") MultipartFile carImage,
	    @RequestParam("agreementFile") MultipartFile agreementFile,
	    @RequestParam("carNumber") String carNumber,
	    @RequestParam("carModel") String carModel,
	    @RequestParam("maxPassengers") int maxPassengers,
	    @RequestParam("color") String color,
	    @RequestParam("carDescription") String carDescription
	) {
	    Long sessionMemberId;

	    try {
	        sessionMemberId = SecurityUtil.getCurrentMemberId(); // ✅ 공통 메서드로 호출
	    } catch (IllegalArgumentException | IllegalStateException e) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
	    }
	    
	    if (carRegistrationService.isCarAlreadyRegistered(sessionMemberId)) {
	        throw new CustomException(CarRegistrationErrorCode.CAR_ALREADY_REGISTERED);
	    }
	    
	    String cleanedCarNumber = cleanCarNumber(carNumber);
	    
	    if(carRegistrationService.isCarNumberExists(cleanedCarNumber)) {
	    	throw new CustomException(CarRegistrationErrorCode.DUPLICATE_CAR_NUMBER);
	    }

	    // ✅ Member 가져오기
	    Member member = memberService.findByMemberId(sessionMemberId);

	    // ✅ 파일 저장 (각 파일의 UUID 저장)
	    String carImageName = carModel + sessionMemberId;
	    String carImageUrl = fileStorageService.saveFile(carImage, "car-images/");
	    String agreementFileName = fileStorageService.saveFile(agreementFile, "agreements/");

	    // ✅ 차량 등록 객체 생성
	    CarRegistration car = new CarRegistration();
	    car.setMember(member);
	    car.setCarNumber(cleanedCarNumber);
	    car.setCarModel(carModel);
	    car.setMaxPassengers(maxPassengers);
	    car.setColor(color);
	    car.setCarDescription(carDescription);
	    car.setImageName(carImageName);
	    car.setImageUrl(carImageUrl);
	    car.setVerifiedFile(agreementFileName); // 가장 최근 파일만 저장

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
	    if (car.isEmpty()) {
	        throw new CustomException(CarRegistrationErrorCode.CAR_NOT_FOUND);
	    }
	    return ResponseEntity.ok(car.get());
	}
	
	@GetMapping("/verified/{memberId}")
	public ResponseEntity<Boolean> isCarVerified(@PathVariable("memberId") Long memberId) {
		if (!carRegistrationService.isCarAlreadyRegistered(memberId)) {
			
			throw new CustomException(CarRegistrationErrorCode.CAR_NOT_FOUND);
		}
		boolean isVerified = carRegistrationService.isVerified(memberId);
		return ResponseEntity.ok(isVerified);
	}
	
	
	// 해당 회원아이디로 차량 등록 조회
	@GetMapping("/member/{memberId}")
	public ResponseEntity<CarRegistrationResponseDTO> getCarByMemberId(@PathVariable("memberId") Long memberId) {
	    Optional<CarRegistration> carOptional = carRegistrationService.getCarByMemberId(memberId);

	    if (carOptional.isEmpty()) {
	    	throw new CustomException(CarRegistrationErrorCode.CAR_NOT_FOUND);
	    }

	    CarRegistration car = carOptional.get();

	    // 현재 로그인한 사용자의 ID 가져오기
	    Long sessionMemberId;
	    try {
	        sessionMemberId = SecurityUtil.getCurrentMemberId();
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	    }

	    // 인증 여부 확인
	    boolean isVerified = carRegistrationService.isVerified(sessionMemberId);

	    // DTO 변환 (인증되지 않은 사용자만 agreementFile 포함)
	    CarRegistrationResponseDTO responseDTO = new CarRegistrationResponseDTO(car, !isVerified);

	    return ResponseEntity.ok(responseDTO);
	}
	
	
	@PutMapping("/update/{carId}")
	public ResponseEntity<?> updateCar(@PathVariable("carId") Long carId, 
	        @RequestBody CarUpdateRequestDTO updateCarRequest) {
	    
	    Long sessionMemberId;
	    try {
	        sessionMemberId = SecurityUtil.getCurrentMemberId(); // ✅ 공통 메서드로 호출
	    } catch (IllegalArgumentException | IllegalStateException e) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
	    }

	    Optional<CarRegistration> carOptional = carRegistrationService.getCarByCarId(carId);
	    if (carOptional.isEmpty()) {
	        throw new CustomException(CarRegistrationErrorCode.CAR_NOT_FOUND);
	    }

	    CarRegistration car = carOptional.get();
	    
	    if (!car.getMember().getMemberId().equals(sessionMemberId)) {
	        throw new CustomException(CarRegistrationErrorCode.ROLE_NOT_MODIFY);
	    }

	    // 🚀 DTO에서 값이 null이 아닌 경우에만 업데이트
	    if (updateCarRequest.getCarNumber() != null) {
	    	String cleanedCarNumber = cleanCarNumber(updateCarRequest.getCarNumber());
	        car.setCarNumber(cleanedCarNumber);
	    }
	    if (updateCarRequest.getCarModel() != null) {
	        car.setCarModel(updateCarRequest.getCarModel());
	    }
	    if (updateCarRequest.getMaxPassengers() != 0) {
	        car.setMaxPassengers(updateCarRequest.getMaxPassengers());
	    }
	    if (updateCarRequest.getColor() != null) {
	        car.setColor(updateCarRequest.getColor());
	    }
	    if (updateCarRequest.getImageUrl() != null) {
	        car.setImageUrl(updateCarRequest.getImageUrl());
	    }
	    if (updateCarRequest.getImageName() != null) {
	        car.setImageName(updateCarRequest.getImageName());
	    }
	    if (updateCarRequest.getVerifiedFile() != null) {
	        car.setVerifiedFile(updateCarRequest.getVerifiedFile());
	    }
	    if (updateCarRequest.getCarDescription() != null) {
	        car.setCarDescription(updateCarRequest.getCarDescription());
	    }

	    CarRegistration updatedCar = carRegistrationService.updateCar(car);
	    return ResponseEntity.ok(new CarUpdateResponseDTO(updatedCar));
	}

	
	//delete
	// 차량 삭제
	@DeleteMapping("/delete/{carId}")
	public ResponseEntity<String> deleteCar(@PathVariable("carId") Long carId) {
	    Long sessionMemberId = SecurityUtil.getCurrentMemberId();

	    CarRegistration car = carRegistrationService.getCarByCarId(carId)
	        .orElseThrow(() -> new CustomException(CarRegistrationErrorCode.CAR_NOT_FOUND));

	    if (!car.getMember().getMemberId().equals(sessionMemberId)) {
	        throw new CustomException(CarRegistrationErrorCode.ROLE_NOT_DELETE);
	    }

	    carRegistrationService.deleteCar(carId);
	    return ResponseEntity.ok("차량이 성공적으로 삭제되었습니다.");
	}

}
