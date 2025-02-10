package com.hifive.bururung.domain.taxi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hifive.bururung.domain.taxi.dto.TaxiShareJoinRequest;
import com.hifive.bururung.domain.taxi.service.ITaxiShareJoinService;
import com.hifive.bururung.domain.taxi.service.ITaxiShareService;
import com.hifive.bururung.global.exception.CustomException;
import com.hifive.bururung.global.exception.errorcode.TaxiShareJoinErrorCode;

@RestController
@RequestMapping("/api/taxi/join")
public class TaxiShareJoinController {
	@Autowired
	ITaxiShareJoinService taxiShareJoinService;
	@Autowired
	ITaxiShareService taxiShareService;
	
	//방 참여자 수
	@GetMapping("/count/{taxiShareId}")
	public ResponseEntity<Integer> getJoinCountByTaxiShareId(@PathVariable("taxiShareId") Long taxiShareId) {
		return ResponseEntity.ok(taxiShareJoinService.getJoinCountByTaxiShareId(taxiShareId));
	}
	
	//참여
	@PostMapping("/insert")
	public ResponseEntity<Void> insertTaxiShareJoin(@RequestBody TaxiShareJoinRequest taxiShareJoinRequest) {
		int duplCnt = taxiShareJoinService.getDuplCntByTaxiShareIdAndMemberId(taxiShareJoinRequest);
		int isHost = taxiShareService.getCountTaxsiShareByIdAndMemberId(taxiShareJoinRequest);
		if(duplCnt<1) {
			if(isHost<1) {
				taxiShareJoinService.insertTaxiShareJoin(taxiShareJoinRequest);
				//알림 보내기 
				//참여자(오픈카톡방 주소), 호스트(참여자 정보) 모두 보내기
				return ResponseEntity.status(HttpStatus.CREATED).build();				
			}else {
				System.out.println("본인이 호스트인 방엔 참여할 수 없음!!");
				throw new CustomException(TaxiShareJoinErrorCode.CANNOT_JOIN_OWN_SHARE);
			}
		}else {
			System.out.println("한사람이 똑같은 방에 참여할 수 없음!!!");
			throw new CustomException(TaxiShareJoinErrorCode.DUPLICATE_JOIN_ATTEMPT);
		}
	}
}
