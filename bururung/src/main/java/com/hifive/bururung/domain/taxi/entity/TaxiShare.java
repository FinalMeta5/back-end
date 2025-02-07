package com.hifive.bururung.domain.taxi.entity;

import java.sql.Date;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaxiShare {
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
}
