package com.hifive.bururung.domain.carshare.participant.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.hifive.bururung.domain.carshare.organizer.entity.CarRegistration;
import com.hifive.bururung.domain.carshare.participant.dto.AllCarListResponse;
import com.hifive.bururung.domain.carshare.participant.dto.AvailableCarShareListResponse;
import com.hifive.bururung.domain.carshare.participant.dto.DriverInformationResponse;
import com.hifive.bururung.domain.carshare.participant.dto.DrivingInformationResponse;
import com.hifive.bururung.domain.carshare.participant.dto.PastParticipationListResponse;
import com.hifive.bururung.domain.carshare.participant.dto.CarInformationResponse;
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
    public String registerCarShare(@RequestParam("carShareRegiId") Long carShareRegiId, @RequestParam("userId") Long userId) {
    	try {
    		int leftCredit = registrationService.findLeftoverCredit(userId);
    		boolean reservation = registrationService.insertRegistration(carShareRegiId, userId);
    		
    		if (leftCredit < 7) {
    			String failMessage = "잔여 크레딧이 부족합니다.";
    			return failMessage;
    		}
    		
    		if (reservation) {
    			String successMessage = "차량 공유 예약에 성공했습니다.";
    			return successMessage;

            } else {
            	String failMessage = "차량 공유 예약에 실패했습니다.";
    			return failMessage;
            }
    	} catch (Exception e) {
    		logger.error("(에러3) 차량 공유 예약에 실패했습니다. : " + e.getMessage());
    		String failMessage = "차량 공유 예약에 실패했습니다.";
			return failMessage;
        }
    }
    
    // 6. 리뷰 평점 조회
    @GetMapping("/rating/{memberId}")
    public Double findRating(@PathVariable("memberId") Long memberId) {
    	Double ratingResult = registrationService.findRating(memberId);
		return ratingResult;
    }
    
    // 7. 잔여 크레딧 조회
    @GetMapping("/checked-credit")
    public ResponseEntity<String> findLeftoverCredit(@RequestParam("userId") Long userId) {
    	
    	try {
    		int leftoverCredit = registrationService.findLeftoverCredit(userId);
        	
        	if(leftoverCredit > 0) {
        		String resultMessage = "잔여 크레딧은 " + leftoverCredit + "입니다.";
    			return ResponseEntity.ok(resultMessage);
        	} else {
        		String resultMessage = "잔여 크레딧은 0 입니다.";
    			return ResponseEntity.ok(resultMessage);
        	}
    	} catch (RuntimeException e) {
    		String message = "(에러) " + + userId + "번 회원의 크레딧 정보가 없습니다." ;
        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message); 
    	} catch (Exception e) {
            logger.error("(에러) 크레딧 정보를 조회하던 중 에러가 발생했습니다. ");  
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // 8. 크레딧 차감
    @PostMapping("/deducted-credit")
    public ResponseEntity<String> insertCreditByCar(@RequestParam("userId") Long userId) {
        try {
        	int leftCredit = registrationService.findLeftoverCredit(userId);
        	
        	if(leftCredit >= 7) {
        		registrationService.insertCreditByCar(userId);
        	     
            	String message = "7 크레딧이 차감되었습니다." ;
                return ResponseEntity.ok(message);
        	} else {
        		String message = "잔여 크레딧이 부족합니다." ;
                return ResponseEntity.ok(message);
        	}
        	
        } catch (RuntimeException e) {
    		String message = "(에러) " + + userId + "번 사용자에 대한 정보가 없습니다. " + e.getMessage() ;
        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message); 
    	} catch (Exception e) {
            logger.error("(에러) 크레딧을 차감하던 중 발생했습니다. ");  
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // 9. 전체 공유 차량 목록 조회 
    @GetMapping("/all-list")
    public ResponseEntity<List<AllCarListResponse>> findAllShareCarList() {
        try {
            List<AllCarListResponse> allCarList = registrationService.findAllShareCarList();
            
            if(allCarList.isEmpty()) {
            	log.info("(정보) 공유 차량 목록이 없습니다.");
            	return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }
            log.info("(성공) 공유 차량 전체 목록 조회에 성공하였습니다.");
            return ResponseEntity.ok(allCarList);
        } catch (Exception e) {
            log.error("(실패) 공유 차량 전체 목록 조회에 실패하였습니다. : ", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
         } 
    }
    
    // 10. 과거 차량 탑승 내역 조회
    @GetMapping("/past-list")
    public ResponseEntity<Object> findPastParticipationList(@RequestParam("userId") Long userId) {
    	try {
    		PastParticipationListResponse pastParticipationResponse = registrationService.findPastParticipationList(userId);
    		
    		if (pastParticipationResponse == null) {
    			String message = "(정보) " + userId + "번 사용자의 과거 탑승 내역이 없습니다." ;
            	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message); 
            }
    		
            return ResponseEntity.ok(pastParticipationResponse);
        } catch (RuntimeException e) {
        	String message = "(정보) " + + userId + "번 사용자의 과거 탑승 내역이 없습니다." ;
        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message); 
    	} catch (Exception e) {
            logger.error("과거 탑승 내역을 조회하던 중 에러가 발생했습니다.");  
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // 11. 오늘 차량 탑승 내역 조회
    @GetMapping("/today-list")
    public ResponseEntity<List<PastParticipationListResponse>> findTodayParticipationList(@RequestParam("userId") Long userId) {
    	List<PastParticipationListResponse> participationList = registrationService.findTodayParticipationList(userId);
        return ResponseEntity.ok(participationList);
    }
    
    // 12. 탑승 여부 탄다로 변경
    @PutMapping("/{carShareJoinId}/state-ok")
    public int updateStateOK(@PathVariable("carShareJoinId") Long carShareJoinId) {
        return registrationService.updateStateOK(carShareJoinId);
    }
    
    // 13. 탑승 여부 안탄다로 변경
    @PutMapping("/{carShareJoinId}/state-no")
    public int updateStateNO(@PathVariable("carShareJoinId") Long carShareJoinId) {
        return registrationService.updateStateNO(carShareJoinId);
    }
    
    // 14. 카테고리 별 공유차량 목록 조회
    @GetMapping("/list/category")
    public ResponseEntity<List<AllCarListResponse>> findByCategoryShareCarList(@RequestParam("category") String category) {
    	try {
    		List<AllCarListResponse> categoryCarList = registrationService.findByCategoryShareCarList(category);
            
            if(categoryCarList == null) {
            	log.info("(정보) 공유 차량 목록이 없습니다.");
            	return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }
            log.info("(성공) 공유 차량 전체 목록 조회에 성공하였습니다.");
            return ResponseEntity.ok(categoryCarList);
        } catch (Exception e) {
            log.error("(실패) 공유 차량 전체 목록 조회에 실패하였습니다. : ", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } 
    }
}
