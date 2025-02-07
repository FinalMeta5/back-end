package com.hifive.bururung.domain.taxi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	public void insertTaxiShareJoin(Long taxiShareId, Long memberId) {
		try {
			taxiShareJoinRepository.insertTaxiShareJoin(taxiShareId, memberId);
		}catch(Exception e) {
			System.out.println("insertTaxiShareJoin 예외--> "+e.getMessage());
		}
	}

	@Override
	public void deleteTaxiShareJoinById(Long tsjId) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getDuplCntByTaxiShareIdAndMemberId(Long taxiShareId, Long memberId) {
		return taxiShareJoinRepository.getDuplCntByTaxiShareIdAndMemberId(taxiShareId, memberId);
	}

}
