package com.hifive.bururung.domain.taxi.service;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hifive.bururung.domain.taxi.dto.TaxiShareJoinRequest;
import com.hifive.bururung.domain.taxi.dto.TaxiShareResponse;
import com.hifive.bururung.domain.taxi.entity.TaxiShare;
import com.hifive.bururung.domain.taxi.repository.ITaxiShareRepository;
import com.hifive.bururung.global.exception.CustomException;
import com.hifive.bururung.global.exception.errorcode.TaxiShareErrorCode;

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
	public List<TaxiShareResponse> getTaxiShareByPickupTime(String pickupTime) {
		List<TaxiShareResponse> list = taxiShareRepository.getTaxiShareByPickupTime(pickupTime);
		return list;
	}

	@Override
	public TaxiShareResponse getTaxiShareById(Long taxiShareId) {
		TaxiShareResponse taxiShareResponse = taxiShareRepository.getTaxiShareById(taxiShareId);
		return taxiShareResponse;
	}

	@Override
	public int getCountTaxsiShareByIdAndMemberId(TaxiShareJoinRequest taxiShareJoinRequest) {
		return taxiShareRepository.getCountTaxsiShareByIdAndMemberId(taxiShareJoinRequest);
	}

	@Override
	public void deleteTaxiShare(TaxiShareJoinRequest taxiShareJoinRequest) {
		try {			
			taxiShareRepository.deleteTaxiShare(taxiShareJoinRequest);
		}catch(Exception e) {
			System.out.println("deleteTaxiShare 예외 발생 --> "+e.getMessage());
			throw new CustomException(TaxiShareErrorCode.TAXI_SHARE_DELETE_FAILED);
		}
		
	}
	
//	public TaxiShare getTaxiShareById(Long id) {
//        return taxiShareRepository.findById(id)
//            .orElseThrow(() -> new CustomException(TaxiShareErrorCode.TAXI_SHARE_NOT_FOUND));
//    }

}
