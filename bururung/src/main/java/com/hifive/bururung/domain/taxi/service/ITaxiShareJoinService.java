package com.hifive.bururung.domain.taxi.service;

import java.util.HashMap;
import java.util.List;

import com.hifive.bururung.domain.taxi.dto.TaxiShareJoinRequest;

public interface ITaxiShareJoinService {
	int getJoinCountByTaxiShareId(Long taxiShareId);
	void insertTaxiShareJoin(TaxiShareJoinRequest taxiShareJoinRequest);
	void deleteTaxiShareJoinById(Long tsjId);
	int getDuplCntByTaxiShareIdAndMemberId(TaxiShareJoinRequest taxiShareJoinRequest);
	List<Long> getMemberIdByTaxiShareId(Long taxiShareId);
	void deleteTaxiShareJoinByTaxiShareId(Long taxiShareId);
	void insertCreditByTaxi(int count, Long memberId);
	int findLeftoverCredit(Long memberId);
	List<HashMap<String, Object>> getCarShareCountByMemberIdAndSysdate();
}
