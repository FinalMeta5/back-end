package com.hifive.bururung.domain.carshare.organizer.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class CarRegistrationMemberDTO {
    private Long carId;
    private String carModel;
    private int maxPassengers;
    private String carNumber;
    private String carDescription;
    private String color;
    private String verified;
    private LocalDateTime createdDate;
    private String imageUrl;
    private String imageName;
    private String verifiedFile;
    private Long memberId;
    private String memberName;
}