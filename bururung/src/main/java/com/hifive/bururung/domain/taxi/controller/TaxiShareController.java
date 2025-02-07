package com.hifive.bururung.domain.taxi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hifive.bururung.domain.taxi.entity.TaxiShare;
import com.hifive.bururung.domain.taxi.service.ITaxiShareService;

@RestController
@RequestMapping("/api/taxi")
public class TaxiShareController {
	@Autowired
	ITaxiShareService taxiShareService;
	
	@GetMapping("/list")
	public ResponseEntity<List<TaxiShare>> findAll(){
		List<TaxiShare> taxiShareList = taxiShareService.findAll();
		return ResponseEntity.ok(taxiShareList);
	}
	
	@PostMapping("/insert")
	public ResponseEntity<Void> insertTaxiShare(@RequestBody TaxiShare taxiShare) {
		System.out.println(taxiShare.toString());
		taxiShareService.insertTaxiShare(taxiShare);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
