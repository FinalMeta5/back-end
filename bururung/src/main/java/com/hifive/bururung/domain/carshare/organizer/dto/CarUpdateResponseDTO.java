package com.hifive.bururung.domain.carshare.organizer.dto;

import com.hifive.bururung.domain.carshare.organizer.entity.CarRegistration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class CarUpdateResponseDTO {
    private Long carId;
    private String carNumber;
    private String carModel;
    private int maxPassengers;
    private String color;
    private String imageUrl;
    private String imageName;
    private String verifiedFile;
    private String carDescription; 
    
    public CarUpdateResponseDTO(CarRegistration car) {
        this.carId = car.getCarId();
        this.carNumber = car.getCarNumber();
        this.carModel = car.getCarModel();
        this.maxPassengers = car.getMaxPassengers();
        this.color = car.getColor();
        this.imageUrl = car.getImageUrl();
        this.imageName = car.getImageName();
        this.verifiedFile = car.getVerifiedFile();
        this.carDescription = car.getCarDescription();
    }
}
