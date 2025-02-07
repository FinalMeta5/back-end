package com.hifive.bururung.domain.taxi.repository;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface ITaxiShareJoinRepository {
	int getJoinCountByTaxiShareId(Long taxiShareId);
	void insertTaxiShareJoin(Long taxiShareId, Long memberId);
	void deleteTaxiShareJoinById(Long tsjId);
	int getDuplCntByTaxiShareIdAndMemberId(Long taxiShareId, Long memberId);
}
