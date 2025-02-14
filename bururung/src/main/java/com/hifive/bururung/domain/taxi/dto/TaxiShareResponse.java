package com.hifive.bururung.domain.taxi.dto;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaxiShareResponse {
	private Long taxiShareId;
	private Long memberId;
	private int passengersNum;
	private String pickupLocation;
	private double latitudePL;
	private double longitudePL;

	private Date pickupTime;

	private Character status;
	private Timestamp createdDate;
	private String destination;
	private double latitudeDS;
	private double longitudeDS;
	private String openchatLink;
	private String openchatCode;
	private String estimatedAmount;
	private String timeNego;

	private String pickupTimeOnly;
	private int currentPassengerNum;
	private String memberNickname;
	private String memberImageUrl;
}
