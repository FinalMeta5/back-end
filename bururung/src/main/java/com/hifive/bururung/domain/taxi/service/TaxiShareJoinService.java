package com.hifive.bururung.domain.taxi.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hifive.bururung.domain.taxi.dto.TaxiShareJoinRequest;
import com.hifive.bururung.domain.taxi.dto.TaxiShareJoinResponse;
import com.hifive.bururung.domain.taxi.entity.TaxiShareEntity;
import com.hifive.bururung.domain.taxi.repository.ITaxiShareJoinRepository;
import com.hifive.bururung.domain.taxi.repository.TaxiShareRepository;
import com.hifive.bururung.global.exception.CustomException;
import com.hifive.bururung.global.exception.errorcode.TaxiShareErrorCode;
import com.hifive.bururung.global.exception.errorcode.TaxiShareJoinErrorCode;

@Service
public class TaxiShareJoinService implements ITaxiShareJoinService {
	@Autowired
	private ITaxiShareJoinRepository taxiShareJoinRepository;
	@Autowired
	private TaxiShareRepository taxiShareRepository;
	
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
		taxiShareJoinRepository.deleteTaxiShareJoinById(taxiShareId);
	}
	
	@Override
	public int findLeftoverCredit(Long memberId) {
		return taxiShareJoinRepository.findLeftoverCredit(memberId);
	}
	
	@Override
	public void insertCreditByTaxi(int count, Long memberId) {
		if(taxiShareJoinRepository.findLeftoverCredit(memberId)>=count) {
			taxiShareJoinRepository.insertCreditByTaxi(count, memberId);
		}else {
			throw new CustomException(TaxiShareJoinErrorCode.CREDIT_DEDUCTED_FAILED);
		}
	}
	//차량공유 알림 스케줄링
	@Override
	public List<HashMap<String, Object>> getCarShareCountByMemberIdAndSysdate() {
		try {
			List<HashMap<String, Object>> list = taxiShareJoinRepository.getCarShareCountByMemberIdAndSysdate();
			return list;
		}catch(Exception e) {
			throw new CustomException(TaxiShareJoinErrorCode.CAR_SHARE_SYSDATE_NOT_FOUND);
		}
	}

	@Override
	public List<TaxiShareJoinResponse> getTaxiShareByMemberIdOnToday(Long memberId) {
		try {
			return taxiShareJoinRepository.getTaxiShareByMemberIdOnToday(memberId);
		}catch(Exception e) {
			throw new CustomException(TaxiShareJoinErrorCode.JOIN_NOT_FOUND);
		}
	}

	@Retryable(
			retryFor = {ObjectOptimisticLockingFailureException.class },
			maxAttempts = 3,
			backoff = @Backoff(delay = 100)
	)
	@Override
	@Transactional
	public void join(TaxiShareJoinRequest taxiShareJoinRequest) {
		TaxiShareEntity taxiShare = taxiShareRepository.findByIdWithLock(taxiShareJoinRequest.getTaxiShareId())
		.orElseThrow(() -> new CustomException(TaxiShareErrorCode.TAXI_SHARE_NOT_FOUND));
		int joinCount = taxiShareJoinRepository.getJoinCountByTaxiShareId(taxiShareJoinRequest.getTaxiShareId());
		
		if(joinCount >= taxiShare.getPassengersNum()) {
			throw new CustomException(TaxiShareJoinErrorCode.FULL_CAPACITY);
		}
		
		// 크레딧 차감
		insertCreditByTaxi(2, taxiShareJoinRequest.getMemberId());
		
		// 택시 조인 insert(참여)
		insertTaxiShareJoin(taxiShareJoinRequest);
	}
}
