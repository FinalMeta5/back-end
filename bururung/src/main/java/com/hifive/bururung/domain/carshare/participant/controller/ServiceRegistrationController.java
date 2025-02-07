package com.hifive.bururung.domain.carshare.participant.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hifive.bururung.domain.carshare.participant.dto.AvailableCarShareListResponse;
import com.hifive.bururung.domain.carshare.participant.service.IServiceRegistrationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/carshare/registration")
public class ServiceRegistrationController {

    private static final Logger logger = LoggerFactory.getLogger(ServiceRegistrationController.class);
    
    private final IServiceRegistrationService registrationService;
    
    // 1. 현재 이용 가능한 공유 차량 목록
    @GetMapping("/available-list")
    public ResponseEntity<List<AvailableCarShareListResponse>> getAvailableCarShareList() {
        try {
            List<AvailableCarShareListResponse> availableCarList = registrationService.getAvailableCarShareList();
            
            if (availableCarList.isEmpty()) {
                // 예외 처리 : 내용이 없으면 204 상태 코드 반환
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            // 성공 처리 : 200 상태 코드와 함께 리스트 반환
            return new ResponseEntity<>(availableCarList, HttpStatus.OK);
        } catch (Exception e) {
            // 예외 처리 : 500 상태 코드와 메시지 반환
            logger.error("Error occurred while fetching available car share list", e);  // 예외 로그 찍기
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
