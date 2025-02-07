package com.hifive.bururung.domain.taxi.service;

public interface ITaxiShareJoinService {
	int getJoinCountByTaxiShareId(Long taxiShareId);
	void insertTaxiShareJoin(Long taxiShareId, Long memberId);
	void deleteTaxiShareJoinById(Long tsjId);
	int getDuplCntByTaxiShareIdAndMemberId(Long taxiShareId, Long memberId);
}
