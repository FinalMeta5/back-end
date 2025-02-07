package com.hifive.bururung.domain.taxi.service;

import java.util.List;

import com.hifive.bururung.domain.taxi.entity.TaxiShare;

public interface ITaxiShareService {
	List<TaxiShare> findAll();
	void insertTaxiShare(TaxiShare taxiShare);
}
