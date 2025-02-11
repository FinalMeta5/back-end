package com.hifive.bururung.domain.carshare.organizer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hifive.bururung.domain.carshare.organizer.dto.CarShareRegiRequestDTO;
import com.hifive.bururung.domain.carshare.organizer.service.ICarShareService;

@RestController
@RequestMapping("/api/car-share")
public class CarShareController {
	
	private final ICarShareService carShareService;
	
	public CarShareController(ICarShareService carShareService) {
		this.carShareService = carShareService;

	}
	@PostMapping("/register")
	public ResponseEntity<String> registerCarShare(@RequestBody CarShareRegiRequestDTO request, Authentication authentication) {
	    System.out.printf("차량 공유 서비스 등록 요청: {}", request.toString());
	    System.out.printf("현재 로그인한 사용자: {}", authentication.getName());
	    System.out.printf("사용자 권한: {}", authentication.getAuthorities());

	    return ResponseEntity.ok("차량 공유 서비스 등록 성공");
	}

}
