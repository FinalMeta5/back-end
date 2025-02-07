package com.hifive.bururung.domain.carshare.organizer.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class CarRegistrationDTO {
	private Long carId;
	private int memberId;
	private String carModel;
	private int maxPassengers;
	private String carNumber;
	private String carDescription;
	private String color;
	private String verified;
	private Date createdDate;
	private String imageUrl;
	private String imageName;
	private String verifiedFile;
}      
