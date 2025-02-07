package com.hifive.bururung.domain.taxi.service;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hifive.bururung.domain.taxi.entity.TaxiShare;
import com.hifive.bururung.domain.taxi.repository.ITaxiShareRepository;

@Service
public class TaxiShareService implements ITaxiShareService{
	
	@Autowired
	ITaxiShareRepository taxiShareRepository;
	@Override
	public List<TaxiShare> findAll() {
		return taxiShareRepository.findAll();
	}

	@Override
	public void insertTaxiShare(TaxiShare taxiShare) {
		try {			
			taxiShareRepository.insertTaxiShare(taxiShare);
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public List<TaxiShare> getTaxiShareByPickupTime(String pickupTime) {
		List<TaxiShare> list = taxiShareRepository.getTaxiShareByPickupTime(pickupTime);
		return list;
	}

	@Override
	public TaxiShare getTaxiShareById(Long taxiShareId) {
		TaxiShare taxiShare = taxiShareRepository.getTaxiShareById(taxiShareId);
		return taxiShare;
	}
	
//	public TaxiShare getTaxiShareById(Long id) {
//        return taxiShareRepository.findById(id)
//            .orElseThrow(() -> new CustomException(TaxiShareErrorCode.TAXI_SHARE_NOT_FOUND));
//    }

}
