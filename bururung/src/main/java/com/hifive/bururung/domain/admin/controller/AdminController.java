package com.hifive.bururung.domain.admin.controller;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
	
	@PostMapping("/reqUpdateRegi")
	public ResponseEntity<String> requestUpdateRegistration(@RequestBody AdminCarRegistrationRequest registration) {
		try {
			Notification notification = new Notification();
			notification.setCategory("인증 실패");
			notification.setServiceCtg("차량 등록");
			notification.setContent("차량을 인증할 수 없습니다.\n다시 작성해주시면 감사하겠습니다.");
			notification.setSenderId(41l);
			notification.setRecipientId(registration.getMemberId());
			adminService.deleteRegistration(registration.getCarId());
			notificationService.sendNotification(notification);
			return ResponseEntity.ok("ok");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@GetMapping("/excel")
    public ResponseEntity<byte[]> downloadExcel() {
    	try {
    		// 1. 결제 데이터를 서비스로부터 가져오기
    		List<AdminPaymentDTO> paymentList = adminService.getPaymentList();
    		System.out.println(paymentList);
    		
    		// 2. Excel 워크북 및 시트 생성
    		Workbook workbook = new XSSFWorkbook();
    		Sheet sheet = workbook.createSheet("Payments");
    		
    		// 3. 헤더 행 생성
    		Row header = sheet.createRow(0);
    		header.createCell(0).setCellValue("Payment ID");
    		header.createCell(1).setCellValue("Order Id");
    		header.createCell(2).setCellValue("Price");
    		header.createCell(3).setCellValue("Member Id");
    		header.createCell(4).setCellValue("Credit Id");
    		header.createCell(5).setCellValue("Method");
    		header.createCell(6).setCellValue("Payment Date");
    		header.createCell(7).setCellValue("Approve Date");
    		
    		// 4. 결제 데이터 리스트를 순회하며 행 생성
    		int rowNum = 1;
    		for (AdminPaymentDTO payment : paymentList) {
    			Row row = sheet.createRow(rowNum++);
    			row.createCell(0).setCellValue(payment.getPaymentId());
    			row.createCell(1).setCellValue(payment.getOrderId());
    			row.createCell(2).setCellValue(payment.getPrice());
    			row.createCell(3).setCellValue(payment.getMemberId());
    			row.createCell(4).setCellValue(payment.getCreditId());
    			row.createCell(5).setCellValue(payment.getMethod());
    			row.createCell(6).setCellValue(payment.getCreatedDate());
    			row.createCell(7).setCellValue(payment.getApprovedDate());
    		}
    		
    		// 5. 워크북을 byte 배열로 변환
    		ByteArrayOutputStream bos = new ByteArrayOutputStream();
    		workbook.write(bos);
    		workbook.close();
    		byte[] excelBytes = bos.toByteArray();
    		
    		// 6. 응답 헤더 설정 (attachment로 다운로드)
    		HttpHeaders headers = new HttpHeaders();
    		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    		ContentDisposition contentDisposition = ContentDisposition
    				.attachment()
    				.filename("payments.xlsx")
    				.build();
    		headers.setContentDisposition(contentDisposition);
    		return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage().getBytes());
		}
    }
}
