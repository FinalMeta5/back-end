package com.hifive.bururung.domain.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hifive.bururung.domain.admin.dto.AdminCarRegistrationDTO;
import com.hifive.bururung.domain.admin.dto.AdminCarRegistrationRequest;
import com.hifive.bururung.domain.admin.dto.AdminCarRegistrationResponse;
import com.hifive.bururung.domain.admin.dto.AdminCarShareDTO;
import com.hifive.bururung.domain.admin.dto.AdminCarShareResponse;
import com.hifive.bururung.domain.admin.dto.AdminMemberDTO;
import com.hifive.bururung.domain.admin.dto.AdminMemberResponse;
import com.hifive.bururung.domain.admin.dto.AdminPaymentDTO;
import com.hifive.bururung.domain.admin.dto.AdminPaymentResponse;
import com.hifive.bururung.domain.admin.dto.AdminTaxiShareDTO;
import com.hifive.bururung.domain.admin.dto.AdminTaxiShareResponse;
import com.hifive.bururung.domain.admin.service.IAdminService;
import com.hifive.bururung.domain.notification.entity.Notification;
import com.hifive.bururung.domain.notification.service.INotificationService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
	
	@Autowired
	private IAdminService adminService;
	@Autowired
	private INotificationService notificationService;
	
	@GetMapping("/members")
	public ResponseEntity<AdminMemberResponse<List<AdminMemberDTO>>> getMemberList() {
		try {
			List<AdminMemberDTO> memberList = adminService.getMemberList();
			return ResponseEntity.ok(new AdminMemberResponse<>(true, memberList, null));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AdminMemberResponse<>(false, null, e.getMessage()));
		}
	}
	
	@GetMapping("/registrations")
	public ResponseEntity<AdminCarRegistrationResponse<List<AdminCarRegistrationDTO>>> getRegistrationList() {
		try {
			List<AdminCarRegistrationDTO> registrationList = adminService.getRegistrationList();
			return ResponseEntity.ok(new AdminCarRegistrationResponse<>(true, registrationList, null));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AdminCarRegistrationResponse<>(false, null, e.getMessage()));
		}
	}

	@GetMapping("/taxi-share")
	public ResponseEntity<AdminTaxiShareResponse<List<AdminTaxiShareDTO>>> getTaxiShareServiceList() {
		try {
			List<AdminTaxiShareDTO> taxiShareServiceList = adminService.getTaxiShareServiceList();
			return ResponseEntity.ok(new AdminTaxiShareResponse<>(true, taxiShareServiceList, null));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AdminTaxiShareResponse<>(false, null, e.getMessage()));
		}
	}
	
	@GetMapping("/car-share")
	public ResponseEntity<AdminCarShareResponse<List<AdminCarShareDTO>>> getCarShareServiceList() {
		try {
			List<AdminCarShareDTO> carShareServiceList = adminService.getCarShareServiceList();
			return ResponseEntity.ok(new AdminCarShareResponse<>(true, carShareServiceList, null));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AdminCarShareResponse<>(false, null, e.getMessage()));
		}
	}
	
	@GetMapping("/payment")
	public ResponseEntity<AdminPaymentResponse<List<AdminPaymentDTO>>> getPaymentList() {
		try {
			List<AdminPaymentDTO> paymentList = adminService.getPaymentList();
			return ResponseEntity.ok(new AdminPaymentResponse<>(true, paymentList, null));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AdminPaymentResponse<>(false, null, e.getMessage()));
		}
	}
	
	@PutMapping("/approve")
	public ResponseEntity<String> approveRegistration(@RequestBody AdminCarRegistrationRequest registration) {
		try {
			adminService.approveRegistration(registration);
			return ResponseEntity.ok("ok");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@PostMapping("/reqUpdateRegi/{memberId}")
	public ResponseEntity<String> requestUpdateRegistration(@PathVariable("memberId") Long memberId) {
		try {
			Notification notification = new Notification();
			notification.setCategory("인증 실패");
			notification.setServiceCtg("차량 등록");
			notification.setContent("차량을 인증할 수 없습니다.\n다시 작성해주시면 감사하겠습니다.");
			notification.setSenderId(41l);
			notification.setRecipientId(memberId);
			notificationService.sendNotification(notification);
			return ResponseEntity.ok("ok");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
}
