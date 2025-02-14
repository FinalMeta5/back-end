package com.hifive.bururung.domain.carshare.organizer.controller;

import java.io.IOException;
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
import com.hifive.bururung.domain.notification.entity.Notification;
import com.hifive.bururung.domain.notification.service.INotificationService;
import com.hifive.bururung.global.common.s3.FileSubPath;
import com.hifive.bururung.global.common.s3.S3Uploader;
import com.hifive.bururung.global.common.s3.UploadFileDTO;
import com.hifive.bururung.global.exception.CustomException;
import com.hifive.bururung.global.exception.errorcode.CarRegistrationErrorCode;
import com.hifive.bururung.global.util.SecurityUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Car Registration", description = "차량 등록 API")
@RestController
@RequestMapping("/api/car-registration")
public class CarRegistrationController {
	private final static Long OPERRATOR_ID = 41L;
	private final ICarRegistrationService carRegistrationService;
	private final MemberService memberService;
	private final S3Uploader s3Uploader;
	private final INotificationService notificationService;

	public CarRegistrationController(ICarRegistrationService carRegistrationService, MemberService memberService,
			S3Uploader s3Uploader, INotificationService notificationService) {
		this.carRegistrationService = carRegistrationService;
		this.memberService = memberService;
		this.s3Uploader = s3Uploader;
		this.notificationService = notificationService;
	}

	// 공백 제거 유틸 메서드
	private String cleanCarNumber(String carNumber) {
		if (carNumber == null)
			return null;
		return carNumber.trim().replaceAll("\\s+", ""); // 모든 공백 제거
	}

	@Operation(summary = "차량 등록 시 차량 사진 S3 업로드")
	@PostMapping("/upload-car-image")
	public ResponseEntity<String> uploadCarImage(@RequestParam("carImage") MultipartFile carImage) throws IOException {
		if (carImage == null) {
			return ResponseEntity.badRequest().body("🚨 업로드할 파일이 없습니다! (NULL)");
		}
		if (carImage.isEmpty()) {
			return ResponseEntity.badRequest().body("🚨 업로드할 파일이 없습니다! (EMPTY)");
		}

		UploadFileDTO uploadFileDTO = s3Uploader.uploadFile(carImage, FileSubPath.CAR.getPath());

		System.out.println("✅ [UPLOAD SUCCESS] S3 업로드 완료, URL: " + uploadFileDTO.getStoreFullUrl());
		return ResponseEntity.ok(uploadFileDTO.getStoreFullUrl());
	}

	@Operation(summary = "차량 등록 시 범죄조회동의서 S3 업로드")
	@PostMapping("/upload-verified-file")
	public ResponseEntity<String> uploadAgreementFile(@RequestParam("agreementFile") MultipartFile verifiedFile)
			throws IOException {
		// S3에 업로드
		UploadFileDTO uploadFileDTO = s3Uploader.uploadFile(verifiedFile, FileSubPath.VERIFIED.getPath());

		return ResponseEntity.ok(uploadFileDTO.getStoreFullUrl()); // 업로드된 URL 반환
	}

	@Operation(summary = "차량 등록")
	@PostMapping("/register")
	public ResponseEntity<String> registerCar(@RequestParam("carImageUrl") String carImageUrl,
			@RequestParam("agreementFile") String agreementFileUrl, @RequestParam("carNumber") String carNumber,
			@RequestParam("carModel") String carModel, @RequestParam("maxPassengers") int maxPassengers,
			@RequestParam("color") String color, @RequestParam("carDescription") String carDescription) {
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

		if (carRegistrationService.isCarNumberExists(cleanedCarNumber)) {
			throw new CustomException(CarRegistrationErrorCode.DUPLICATE_CAR_NUMBER);
		}

		// Member 가져오기
		Member member = memberService.findByMemberId(sessionMemberId);

		String carImageName = carModel + sessionMemberId;

		// 차량 등록 객체 생성
		CarRegistration car = new CarRegistration();
		car.setMember(member);
		car.setCarNumber(cleanedCarNumber);
		car.setCarModel(carModel);
		car.setMaxPassengers(maxPassengers);
		car.setColor(color);
		car.setCarDescription(carDescription);
		car.setImageName(carImageName);
		car.setImageUrl(carImageUrl);
		car.setVerifiedFile(agreementFileUrl); // 가장 최근 파일만 저장

		carRegistrationService.registerCar(car);

		Notification notification = new Notification();
		notification.setSenderId(sessionMemberId);
		notification.setRecipientId(OPERRATOR_ID);
		notification.setContent("새로운 차량 등록 요청입니다. \n [ 차량 번호 : " + cleanedCarNumber + " ]");
		notification.setCategory("인증 요청");
		notification.setServiceCtg("차량 등록");

		notificationService.sendNotification(notification);
		System.out.println(notification);

		return ResponseEntity.ok("차량이 성공적으로 등록되었습니다.");
	}

	@Operation(summary = "차량 재등록 알림 요청")
	@PostMapping("/re-registration/{memberId}")
	public ResponseEntity<String> sendReRegiInfoByOperator() {

		Long sessionMemberId;
		try {
			sessionMemberId = SecurityUtil.getCurrentMemberId(); // 공통 메서드로 호출
		} catch (IllegalArgumentException | IllegalStateException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		}
		Member member = memberService.findByMemberId(sessionMemberId);
		String memberName = member.getName();

		Notification notification = new Notification();
		notification.setSenderId(sessionMemberId);
		notification.setRecipientId(OPERRATOR_ID);
		notification.setContent(" 차량 재등록 요청입니다. \n [ 회원 이름 : " + memberName + " ]");
		notification.setCategory("인증 재요청");
		notification.setServiceCtg("차량 재등록");

		notificationService.sendNotification(notification);
		System.out.println(notification);

		return ResponseEntity.ok("재등록 요청이 완료되었습니다.");
	}

	@Operation(summary = "전체 차량 리스트 조회")
	@GetMapping("/list")
	public ResponseEntity<List<CarRegistration>> getAllCars() {
		return ResponseEntity.ok(carRegistrationService.getAllCars());
	}

	@Operation(summary = "차량 번호로 차량 정보 조회")
	@GetMapping("/car/by-number/{carNumber}")
	public ResponseEntity<CarRegistration> getCarByCarNimber(@PathVariable("carNumber") String carNumber) {
		Optional<CarRegistration> car = carRegistrationService.getCarByCarNumber(carNumber);
		return car.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@Operation(summary = "차량 아이디로 차량 정보 조회")
	@GetMapping("/car/by-id/{carId}")
	public ResponseEntity<CarRegistration> getCarByCarId(@PathVariable("carId") Long carId) {
		Optional<CarRegistration> car = carRegistrationService.getCarByCarId(carId);
		if (car.isEmpty()) {
			throw new CustomException(CarRegistrationErrorCode.CAR_NOT_FOUND);
		}
		return ResponseEntity.ok(car.get());
	}

	@Operation(summary = "회원 아이디로 인증 여부 조회")
	@GetMapping("/verified/{memberId}")
	public ResponseEntity<Boolean> isCarVerified(@PathVariable("memberId") Long memberId) {
		if (!carRegistrationService.isCarAlreadyRegistered(memberId)) {

			throw new CustomException(CarRegistrationErrorCode.CAR_NOT_FOUND);
		}
		boolean isVerified = carRegistrationService.isVerified(memberId);
		return ResponseEntity.ok(isVerified);
	}

	@Operation(summary = "회원 아이디로 차량 등록 여부 조회")
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

	@Operation(summary = "차량 아이디로 차 정보 수정")
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

	@Operation(summary = "차량 삭제")
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
