package com.hifive.bururung.domain.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminCarRegistrationDTO {
	private Long carId;
	private int memberId;
	private String name;
	private String carModel;
	private int maxPassengers;
	private String carNumber;
	private String carDescription;
	private String color;
	private String createdDate;
	private String imageUrl;
	private String imageName;
	private String verifiedFile;
}
