package com.hifive.bururung.domain.carshare.organizer.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.hifive.bururung.domain.carshare.organizer.entity.CarRegistration;
import com.hifive.bururung.domain.carshare.organizer.repository.CarRegistrationRepository;
import com.hifive.bururung.domain.carshare.organizer.repository.CarShareMapper;
import com.hifive.bururung.global.common.s3.S3Uploader;
import com.hifive.bururung.global.common.s3.UploadFileDTO;
import com.hifive.bururung.global.exception.CustomException;
import com.hifive.bururung.global.exception.errorcode.CarRegistrationErrorCode;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CarRegistrationService implements ICarRegistrationService {
	private final CarRegistrationRepository carRegistrationRepository;
	private final S3Uploader s3Uploader;
	private final CarShareMapper  carShareMapper ;
	
	public CarRegistrationService(CarRegistrationRepository carRegistrationRepository, S3Uploader s3Uploader, 
			CarShareMapper carShareMapper) {
		this.carRegistrationRepository = carRegistrationRepository;
		this.s3Uploader = s3Uploader;
		this.carShareMapper  = carShareMapper ;
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
        // 1. car_share 테이블에서 해당 carId가 존재하는지 확인
        if (carShareMapper.countByCarId(carId) > 0) {
            // 2. 존재하면 해당 carId를 0으로 변경
        	carShareMapper.updateCarIdToZero(carId);
        }

        // 3. 차량 삭제
        carRegistrationRepository.deleteById(carId);
    }


	@Override
	@Transactional
	public boolean isCarNumberExists(String carNumber) {
	    Long count = carRegistrationRepository.countByCarNumber(carNumber);
	    return count != null && count > 0;
	}


	@Override
	@Transactional
    public String uploadCarImage(MultipartFile multipartFile, String subpath, Long memberId) throws IOException {
        CarRegistration car = carRegistrationRepository.findByMember_MemberId(memberId)
            .orElseThrow(() -> new CustomException(CarRegistrationErrorCode.CAR_NOT_FOUND));

        // ✅ 기존 이미지가 있다면 삭제
        if (car.getImageUrl() != null && StringUtils.hasText(car.getImageUrl())) {
            s3Uploader.deleteFile(subpath + car.getImageName());
        }

        // ✅ 새 이미지 업로드
        UploadFileDTO uploadFileDTO = s3Uploader.uploadFile(multipartFile, subpath);
        car.setImageName(uploadFileDTO.getStoreFileName());
        car.setImageUrl(uploadFileDTO.getStoreFullUrl());

        return uploadFileDTO.getStoreFullUrl();
    }


	@Override
	public String uploadVerifiedFile(MultipartFile multipartFile, String subpath, Long memberId) throws IOException {
        CarRegistration car = carRegistrationRepository.findByMember_MemberId(memberId)
                .orElseThrow(() -> new CustomException(CarRegistrationErrorCode.CAR_NOT_FOUND));
            // ✅ 새 파일 업로드
            UploadFileDTO uploadFileDTO = s3Uploader.uploadFile(multipartFile, subpath);
            car.setVerifiedFile(uploadFileDTO.getStoreFileName());
            car.setVerifiedFile(uploadFileDTO.getStoreFullUrl());

            return uploadFileDTO.getStoreFullUrl();
	}





}
