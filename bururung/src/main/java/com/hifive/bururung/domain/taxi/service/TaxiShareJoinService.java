package com.hifive.bururung.domain.taxi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hifive.bururung.domain.taxi.dto.TaxiShareJoinRequest;
import com.hifive.bururung.domain.taxi.repository.ITaxiShareJoinRepository;
import com.hifive.bururung.global.exception.CustomException;
import com.hifive.bururung.global.exception.errorcode.TaxiShareJoinErrorCode;

@Service
public class TaxiShareJoinService implements ITaxiShareJoinService {
	@Autowired
	ITaxiShareJoinRepository taxiShareJoinRepository;
	
	@Override
	public int getJoinCountByTaxiShareId(Long taxiShareId) {
		try {
			return taxiShareJoinRepository.getJoinCountByTaxiShareId(taxiShareId);
		}catch(Exception e) {
			System.out.println("getJoinCountByTaxiShareId 예외--> "+e.getMessage());
			throw new CustomException(TaxiShareJoinErrorCode.JOIN_NOT_FOUND);
		}
	}

	@Override
	public void insertTaxiShareJoin(TaxiShareJoinRequest taxiShareJoinRequest) {
		try {
			taxiShareJoinRepository.insertTaxiShareJoin(taxiShareJoinRequest);
		}catch(Exception e) {
			System.out.println("insertTaxiShareJoin 예외--> "+e.getMessage());
		}
	}

	@Override
	public void deleteTaxiShareJoinById(Long tsjId) {
		//안쓸거같음..
	}

	@Override
	public int getDuplCntByTaxiShareIdAndMemberId(TaxiShareJoinRequest taxiShareJoinRequest) {
		return taxiShareJoinRepository.getDuplCntByTaxiShareIdAndMemberId(taxiShareJoinRequest);
	}

	@Override
	public List<Long> getMemberIdByTaxiShareId(Long taxiShareId) {
		return taxiShareJoinRepository.getMemberIdByTaxiShareId(taxiShareId);
	}

	@Override
	public void deleteTaxiShareJoinByTaxiShareId(Long taxiShareId) {
		try {			
			taxiShareJoinRepository.deleteTaxiShareJoinById(taxiShareId);
		}catch(Exception e) {
			System.out.println("deleteTaxiShareJoinByTaxiShareId 예외: ==> "+e.getMessage());
		}
		
	}

}
