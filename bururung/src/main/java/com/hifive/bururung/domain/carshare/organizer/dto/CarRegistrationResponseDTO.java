package com.hifive.bururung.domain.carshare.organizer.dto;

import com.hifive.bururung.domain.carshare.organizer.entity.CarRegistration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarRegistrationResponseDTO {
    private Long carId;
    private String carNumber;
    private String carModel;
    private int maxPassengers;
    private String color;
    private String carDescription;
    private String imageUrl;
    private boolean isVerified;
    private String agreementFile; // 인증 안된 사용자만 보이게 설정

    public CarRegistrationResponseDTO(CarRegistration car, boolean showAgreementFile) {
        this.carId = car.getCarId();
        this.carNumber = car.getCarNumber();
        this.carModel = car.getCarModel();
        this.maxPassengers = car.getMaxPassengers();
        this.color = car.getColor();
        this.carDescription = car.getCarDescription();
        this.imageUrl = car.getImageUrl();
        this.isVerified = "Y".equals(car.getVerified());

        // 인증되지 않은 사용자만 agreementFile 반환
        this.agreementFile = showAgreementFile ? car.getVerifiedFile() : null;
    }
}
