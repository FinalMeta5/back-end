package com.hifive.bururung.domain.taxi.service;

import java.sql.Date;
import java.util.List;

import com.hifive.bururung.domain.taxi.dto.TaxiShareJoinRequest;
import com.hifive.bururung.domain.taxi.dto.TaxiShareResponse;
import com.hifive.bururung.domain.taxi.entity.TaxiShare;

public interface ITaxiShareService {
	List<TaxiShare> findAll();
	void insertTaxiShare(TaxiShare taxiShare);
	List<TaxiShareResponse> getTaxiShareByPickupTime(String pickupTime);
	TaxiShareResponse getTaxiShareById(Long taxiShareId);
	int getCountTaxsiShareByIdAndMemberId(TaxiShareJoinRequest taxiShareJoinRequest);
	void deleteTaxiShare(TaxiShareJoinRequest taxiShareJoinRequest);
}
