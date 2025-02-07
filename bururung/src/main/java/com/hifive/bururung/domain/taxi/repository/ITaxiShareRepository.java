package com.hifive.bururung.domain.taxi.repository;

import java.sql.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.hifive.bururung.domain.taxi.entity.TaxiShare;
@Mapper
@Repository
public interface ITaxiShareRepository {
	List<TaxiShare> findAll();
	void insertTaxiShare(TaxiShare taxiShare);
	List<TaxiShare> getTaxiShareByPickupTime(String pickupTime);
	TaxiShare getTaxiShareById(Long taxiShareId);
}
