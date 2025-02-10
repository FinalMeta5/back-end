package com.hifive.bururung.domain.taxi.repository;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.hifive.bururung.domain.taxi.dto.TaxiShareJoinRequest;

@Repository
@Mapper
public interface ITaxiShareJoinRepository {
	int getJoinCountByTaxiShareId(Long taxiShareId);
	void insertTaxiShareJoin(TaxiShareJoinRequest taxiShareJoinRequest);
	void deleteTaxiShareJoinById(Long tsjId);
	int getDuplCntByTaxiShareIdAndMemberId(TaxiShareJoinRequest taxiShareJoinRequest);
	List<Long> getMemberIdByTaxiShareId(Long taxiShareId);
}
