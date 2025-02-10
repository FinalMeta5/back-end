package com.hifive.bururung.domain.carshare.participant.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.hifive.bururung.domain.carshare.organizer.entity.CarRegistration;
import com.hifive.bururung.domain.carshare.participant.dto.AvailableCarShareListResponse;
import com.hifive.bururung.domain.carshare.participant.dto.DriverInformationResponse;
import com.hifive.bururung.domain.carshare.participant.dto.DrivingInformationResponse;
import com.hifive.bururung.domain.carshare.participant.dto.CarInformationResponse;
import com.hifive.bururung.domain.carshare.participant.dto.CarShareRegistrationRequest;
import com.hifive.bururung.domain.carshare.participant.service.IServiceRegistrationService;
import com.hifive.bururung.domain.member.entity.Member;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/carshare/registration")
//@CrossOrigin(origins = "http://localhost:3000")
@CrossOrigin(origins = "https://bururung-2911d.web.app")
@Slf4j
public class ServiceRegistrationController {

    private static final Logger logger = LoggerFactory.getLogger(ServiceRegistrationController.class);
    
    private final IServiceRegistrationService registrationService;
    
    // 1. 현재 이용 가능한 공유 차량 목록
    @GetMapping("/available-list")
    public ResponseEntity<List<AvailableCarShareListResponse>> getAvailableCarShareList() {
        try {
            List<AvailableCarShareListResponse> availableCarList = registrationService.getAvailableCarShareList();
            
            if(availableCarList.isEmpty()) {
            	log.info("(Info) No available cars to display");
            	return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }
            log.info("(Success) display shared car list.");
            return ResponseEntity.ok(availableCarList);
        } catch (Exception e) {
            log.error("(Fail) Error occurred while fetching available car share list : ", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
          } 
    }
    
    // 2. 운전자 정보
    @GetMapping("/driver-information/{memberId}")
    public ResponseEntity<Object> getCarShareDetailInformation(@PathVariable("memberId") Long memberId) {
    	try {
    		DriverInformationResponse driverInformationResponse = registrationService.getDriverInformation(memberId);
    		
    		if (driverInformationResponse == null) {
    			String message = "(정보) " + + memberId + "번 사용자는 운전자로 등록되어 있지 않습니다." ;
            	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message); 
            }
    		
            return ResponseEntity.ok(driverInformationResponse);
        } catch (RuntimeException e) {
        	String message = "(정보) " + + memberId + "번 사용자는 운전자로 등록되어 있지 않습니다." ;
        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message); 
    	} catch (Exception e) {
            logger.error("운전자 정보를 조회하던 중 에러가 발생했습니다.");  
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // 3. 차량 정보
    @GetMapping("/car-information/{memberId}")
    public ResponseEntity<Object> getCarInformation(@PathVariable("memberId") Long memberId) {
    	try {
    		CarInformationResponse carInformationResponse = registrationService.getCarInformation(memberId);
    		
    		if (carInformationResponse == null) {
    			String message = "(정보) " + + memberId + "번 사용자의 차량은 등록되어 있지 않습니다." ;
            	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message); 
            }
    		
    		return ResponseEntity.ok(carInformationResponse);
    	} catch (RuntimeException e) {
    		String message = "(정보) " + + memberId + "번 사용자의 차량은 등록되어 있지 않습니다." ;
        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message); 
    	} catch (Exception e) {
            logger.error("(에러) 차량 정보를 조회하던 중 에러가 발생했습니다.");  
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
  
    // 4. 치량 운행 정보
    @GetMapping("/driving-information")
    public ResponseEntity<Object> getDrivingInformation(@RequestParam("memberId") Long memberId, @RequestParam("carShareRegiId") Long carShareRegiId) {
    	try {
    		DrivingInformationResponse drivingInformation = registrationService.getDrivingInformation(memberId, carShareRegiId);

    		if(drivingInformation == null) {
    			String message = "(정보) " + + carShareRegiId + "번 차량 공유에 대한 운행 정보가 없습니다." ;
            	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message); 
    		}
    		
    		return ResponseEntity.ok(drivingInformation);
    	} catch (RuntimeException e) {
    		String message = "(정보) " + + carShareRegiId + "번 차량 공유에 대한 운행 정보가 없습니다." ;
        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message); 
    	} catch (Exception e) {
            logger.error("(에러) 차량 운행 정보를 조회하던 중 에러가 발생했습니다. ");  
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // 5. 차량 공유 예약
    @PostMapping("/reservation")
    public ResponseEntity<String> registerCarShare(@RequestBody CarShareRegistrationRequest request) {
    	try {
    		registrationService.insertRegistration(request);
    		return ResponseEntity.ok("(성공) 차량 공유 예약에 성공했습니다.");
    	} catch (Exception e) {
            logger.error("(에러) 공유 차량 예약에 실패했습니다.");  
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
