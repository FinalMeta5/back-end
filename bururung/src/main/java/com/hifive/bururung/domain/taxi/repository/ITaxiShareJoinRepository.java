package com.hifive.bururung.domain.taxi.repository;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.hifive.bururung.domain.taxi.dto.TaxiShareJoinRequest;
import com.hifive.bururung.domain.taxi.dto.TaxiShareJoinResponse;

@Repository
@Mapper
public interface ITaxiShareJoinRepository {
	int getJoinCountByTaxiShareId(Long taxiShareId);
	void insertTaxiShareJoin(TaxiShareJoinRequest taxiShareJoinRequest);
	void deleteTaxiShareJoinById(Long tsjId);
	int getDuplCntByTaxiShareIdAndMemberId(TaxiShareJoinRequest taxiShareJoinRequest);
	List<Long> getMemberIdByTaxiShareId(Long taxiShareId);
	void deleteTaxiShareJoinByTaxiShareId(Long taxiShareId);
	void insertCreditByTaxi(int count, Long memberId);
	int findLeftoverCredit(Long memberId);
	List<TaxiShareJoinResponse> getTaxiShareByMemberIdOnToday(Long memberId);
	//차량공유 알림스케쥴링
	List<HashMap<String, Object>> getCarShareCountByMemberIdAndSysdate();
}
