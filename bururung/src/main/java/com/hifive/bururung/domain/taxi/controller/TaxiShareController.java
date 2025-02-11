package com.hifive.bururung.domain.taxi.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hifive.bururung.domain.member.entity.Member;
import com.hifive.bururung.domain.member.service.IMemberService;
import com.hifive.bururung.domain.notification.entity.Notification;
import com.hifive.bururung.domain.notification.service.INotificationService;
import com.hifive.bururung.domain.taxi.dto.LatLng;
import com.hifive.bururung.domain.taxi.dto.TaxiShareJoinRequest;
import com.hifive.bururung.domain.taxi.dto.TaxiShareResponse;
import com.hifive.bururung.domain.taxi.entity.TaxiShare;
import com.hifive.bururung.domain.taxi.service.ITaxiShareJoinService;
import com.hifive.bururung.domain.taxi.service.ITaxiShareService;
import com.hifive.bururung.domain.taxi.util.TaxiShareJoinAction;
import com.hifive.bururung.global.common.NearbyLocationFinder;

@RestController
@RequestMapping("/api/taxi")
public class TaxiShareController {
	@Autowired
	ITaxiShareService taxiShareService;
	@Autowired
	ITaxiShareJoinService taxiShareJoinService;
	@Autowired
	IMemberService memberService;
	@Autowired
	INotificationService notificationService;
	
	@PostMapping("/insert")
	public ResponseEntity<Void> insertTaxiShare(@RequestBody TaxiShare taxiShare) {
		System.out.println(taxiShare.toString());
		taxiShareService.insertTaxiShare(taxiShare);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@PostMapping("/list")
	public ResponseEntity<List<TaxiShareResponse>> getTaxiShareByPickupTimeAndLatLng(
			@RequestParam("pickupTime")String pickupTime, @RequestBody(required=false) LatLng target){
		
		if (target == null) {
			//날짜로만 검색
	        List<TaxiShareResponse> list = taxiShareService.getTaxiShareByPickupTime(pickupTime);
	        return ResponseEntity.ok(list);
	    }else {
	    	List<TaxiShareResponse> list = taxiShareService.getTaxiShareByPickupTime(pickupTime);
	    	//날짜로 검색한 리스트들 중에 위도경도 정보만 빼서 따로 리스트로 저장
	    	List<LatLng> allLocations = new ArrayList<>();
	        for (TaxiShareResponse taxiShareResponse : list) {
	            allLocations.add(new LatLng(taxiShareResponse.getLatitudePL(), taxiShareResponse.getLongitudePL()));
	        }
	        //타겟 지점이랑 계산해서 해당하는 위치 리스트
	    	List<LatLng> nearbyLocations = NearbyLocationFinder.findNearbyLocations(target, allLocations);
	    	//그 위치에 해당하는 객체리스트
	    	List<TaxiShareResponse> nearbyTaxiShares = new ArrayList<>();
	    	
	    	for(TaxiShareResponse taxiShareResponse: list) {
	    		boolean isNearby = false;
	    		for(LatLng latlng : nearbyLocations) {
	    			if(latlng.getLat()==taxiShareResponse.getLatitudePL() && latlng.getLng()==taxiShareResponse.getLongitudePL()) {
	    				isNearby = true;
	    				break;
	    			}
	    		}
	    		if(isNearby) {
	    			nearbyTaxiShares.add(taxiShareResponse);
	    		}
	    	}
	    	
	    	return ResponseEntity.ok(nearbyTaxiShares);
	    }
	}
	
	@GetMapping("/detail/{taxiShareId}")
	public ResponseEntity<TaxiShareResponse> getTaxiShareById(@PathVariable("taxiShareId") Long taxiShareId){
		TaxiShareResponse taxiShareResponse = taxiShareService.getTaxiShareById(taxiShareId);
		return ResponseEntity.ok(taxiShareResponse);
	}
	
	//택시 공유 삭제
	@PostMapping("/delete")
	public ResponseEntity<Void> deleteTaxiShare(@RequestBody TaxiShareJoinRequest taxiShareJoinRequest) {
		//삭제되는 해당 택시공유 정보
		TaxiShareResponse taxiShareResponse = taxiShareService.getTaxiShareById(taxiShareJoinRequest.getTaxiShareId());
		System.out.println("택시 공유 삭제하려는 사람 : "+taxiShareJoinRequest.getMemberId()+", 실제 삭제하는 게시글 쓴사람"+taxiShareResponse.getMemberId());
		//알람 보낼 멤버리스트(삭제하는 택시공유에 신청한 멤버들)
		List<Long> memberList = taxiShareJoinService.getMemberIdByTaxiShareId(taxiShareJoinRequest.getTaxiShareId());
		System.out.println("전송해야되는 멤버리스트: "+memberList.toString());
		for(Long memberId : memberList) {
			Member participantInfo = memberService.findByMemberId(memberId);
			Notification notification = TaxiShareJoinAction.getTaxiShareDeleteNotiInfo(taxiShareResponse, taxiShareJoinRequest, participantInfo);
			notificationService.sendNotification(notification);
		}
		//택시 삭제하기
		taxiShareService.deleteTaxiShare(taxiShareJoinRequest);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
