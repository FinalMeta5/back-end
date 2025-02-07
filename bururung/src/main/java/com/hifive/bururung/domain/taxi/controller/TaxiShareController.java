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

import com.hifive.bururung.domain.taxi.dto.LatLng;
import com.hifive.bururung.domain.taxi.entity.TaxiShare;
import com.hifive.bururung.domain.taxi.service.ITaxiShareService;
import com.hifive.bururung.global.common.NearbyLocationFinder;

@RestController
@RequestMapping("/api/taxi")
public class TaxiShareController {
	@Autowired
	ITaxiShareService taxiShareService;
	
//	@GetMapping("/list")
//	public ResponseEntity<List<TaxiShare>> findAll(){
//		List<TaxiShare> taxiShareList = taxiShareService.findAll();
//		return ResponseEntity.ok(taxiShareList);
//	}
	
	@PostMapping("/insert")
	public ResponseEntity<Void> insertTaxiShare(@RequestBody TaxiShare taxiShare) {
		System.out.println(taxiShare.toString());
		taxiShareService.insertTaxiShare(taxiShare);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@PostMapping("/list")
	public ResponseEntity<List<TaxiShare>> getTaxiShareByPickupTimeAndLatLng(
			@RequestParam("pickupTime")String pickupTime, @RequestBody(required=false) LatLng target){
		
		if (target == null) {
			//날짜로만 검색
	        List<TaxiShare> list = taxiShareService.getTaxiShareByPickupTime(pickupTime);
	        return ResponseEntity.ok(list);
	    }else {
	    	//날짜와 지역 모두 검색
	    	List<TaxiShare> list = taxiShareService.getTaxiShareByPickupTime(pickupTime);
	    	//날짜로 검색한 리스트들 중에 위도경도 정보만 빼서 따로 리스트로 저장
	    	List<LatLng> allLocations = new ArrayList<>();
	        for (TaxiShare taxiShare : list) {
	            allLocations.add(new LatLng(taxiShare.getLatitudePL(), taxiShare.getLongitudePL()));
	        }
	        //타겟 지점이랑 계산해서 해당하는 위치 리스트
	    	List<LatLng> nearbyLocations = NearbyLocationFinder.findNearbyLocations(target, allLocations);
	    	//그 위치에 해당하는 객체리스트
	    	List<TaxiShare> nearbyTaxiShares = new ArrayList<>();
	    	
	    	for(TaxiShare taxiShare: list) {
	    		boolean isNearby = false;
	    		for(LatLng latlng : nearbyLocations) {
	    			if(latlng.getLat()==taxiShare.getLatitudePL() && latlng.getLng()==taxiShare.getLongitudePL()) {
	    				isNearby = true;
	    				break;
	    			}
	    		}
	    		if(isNearby) {
	    			nearbyTaxiShares.add(taxiShare);
	    		}
	    	}
	    	
	    	return ResponseEntity.ok(nearbyTaxiShares);
	    }
	}
	
	@GetMapping("/detail/{taxiShareId}")
	public ResponseEntity<TaxiShare> getTaxiShareById(@PathVariable("taxiShareId") Long taxiShareId){
		TaxiShare taxiShare = taxiShareService.getTaxiShareById(taxiShareId);
		return ResponseEntity.ok(taxiShare);
	}
}
