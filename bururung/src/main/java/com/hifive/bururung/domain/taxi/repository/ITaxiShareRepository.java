package com.hifive.bururung.domain.taxi.repository;

import java.sql.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hifive.bururung.domain.taxi.dto.TaxiShareJoinRequest;
import com.hifive.bururung.domain.taxi.dto.TaxiShareResponse;
import com.hifive.bururung.domain.taxi.entity.TaxiShare;
@Mapper
@Repository
public interface ITaxiShareRepository {
	List<TaxiShare> findAll();
	void insertTaxiShare(TaxiShare taxiShare);
	List<TaxiShareResponse> getTaxiShareByPickupTime(String pickupTime);
	TaxiShareResponse getTaxiShareById(Long taxiShareId);
	int getCountTaxsiShareByIdAndMemberId(TaxiShareJoinRequest taxiShareJoinRequest);
	@Transactional
	int deleteTaxiShare(TaxiShareJoinRequest taxiShareJoinRequest);
}
