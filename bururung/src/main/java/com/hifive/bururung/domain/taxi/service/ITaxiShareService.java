package com.hifive.bururung.domain.taxi.service;

import java.sql.Date;
import java.util.List;

import com.hifive.bururung.domain.taxi.entity.TaxiShare;

public interface ITaxiShareService {
	List<TaxiShare> findAll();
	void insertTaxiShare(TaxiShare taxiShare);
	List<TaxiShare> getTaxiShareByPickupTime(String pickupTime);
	TaxiShare getTaxiShareById(Long taxiShareId);
}
