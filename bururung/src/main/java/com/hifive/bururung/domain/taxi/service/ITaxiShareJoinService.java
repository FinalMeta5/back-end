package com.hifive.bururung.domain.taxi.service;

import com.hifive.bururung.domain.taxi.dto.TaxiShareJoinRequest;

public interface ITaxiShareJoinService {
	int getJoinCountByTaxiShareId(Long taxiShareId);
	void insertTaxiShareJoin(TaxiShareJoinRequest taxiShareJoinRequest);
	void deleteTaxiShareJoinById(Long tsjId);
	int getDuplCntByTaxiShareIdAndMemberId(TaxiShareJoinRequest taxiShareJoinRequest);
}
