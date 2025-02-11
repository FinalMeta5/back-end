package com.hifive.bururung.domain.payment.controller;

import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.hifive.bururung.domain.credit.service.IMemberCreditService;
import com.hifive.bururung.domain.payment.dto.ConfirmRequest;
import com.hifive.bururung.domain.payment.dto.PaymentConfirmResponse;
import com.hifive.bururung.domain.payment.dto.PaymentRequest;
import com.hifive.bururung.domain.payment.repository.PaymentRepository;
import com.hifive.bururung.domain.payment.service.IPaymentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {
	
	private final IPaymentService paymentService;
	private final IMemberCreditService memberCreditService;
	private final PaymentRepository paymentRepository;
	
	@PostMapping("/request")
	public ResponseEntity<Long> requestPayment(@RequestBody PaymentRequest paymentRequest) {
		Long paymentId = paymentService.requestPayment(paymentRequest);
		
		return ResponseEntity.ok(paymentId);
	}
	
	@PostMapping("/confirm")
	public ResponseEntity<PaymentConfirmResponse> confirmPayment(@AuthenticationPrincipal User user, @RequestBody ConfirmRequest confirmRequest) throws Exception {
		Long memberId = Long.parseLong(user.getUsername());
		
		Long paymentId = paymentService.verify(confirmRequest.getPaymentKey(), confirmRequest.getOrderId(), confirmRequest.getPrice());
		
		PaymentConfirmResponse paymentConfirmResponse = paymentService.requestConfirm(confirmRequest.getPaymentKey(), confirmRequest.getOrderId(), confirmRequest.getPrice());
		
		int creditCount = paymentRepository.findCreditCount(paymentId);
		memberCreditService.chargeCredit(memberId, creditCount);
		
		return ResponseEntity.ok(paymentConfirmResponse);
	}
}
