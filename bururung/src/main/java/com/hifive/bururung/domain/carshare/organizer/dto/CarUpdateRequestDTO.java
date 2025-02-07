package com.hifive.bururung.domain.carshare.organizer.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CarUpdateRequestDTO {
    private String carNumber;
    private String carModel;
    private int maxPassengers;
    private String color;
    private String imageUrl;
    private String imageName;
    private String verifiedFile;
    private String carDescription;
}
