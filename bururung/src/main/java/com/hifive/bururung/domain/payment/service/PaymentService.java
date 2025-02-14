package com.hifive.bururung.domain.payment.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.hifive.bururung.domain.credit.entity.Credit;
import com.hifive.bururung.domain.credit.repository.CreditRepository;
import com.hifive.bururung.domain.member.entity.Member;
import com.hifive.bururung.domain.member.repository.MemberRepository;
import com.hifive.bururung.domain.payment.dto.PaymentConfirmResponse;
import com.hifive.bururung.domain.payment.dto.PaymentRequest;
import com.hifive.bururung.domain.payment.entity.Payment;
import com.hifive.bururung.domain.payment.repository.PaymentRepository;
import com.hifive.bururung.global.exception.CustomException;
import com.hifive.bururung.global.exception.errorcode.MemberErrorCode;
import com.hifive.bururung.global.exception.errorcode.PaymentErrorCode;

import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService implements IPaymentService {

	private final PaymentRepository paymentRepository;
	private final CreditRepository creditRepository;
	private final MemberRepository memberRepository;
	
	@Value("${payment.secret}")
	private String secretKey;
	
	@Override
	@Transactional
	public Long requestPayment(PaymentRequest paymentRequest) {
		Credit credit = creditRepository.findByCount(paymentRequest.getCreditCount())
				.orElseThrow(() -> new CustomException(PaymentErrorCode.CREDIT_NOT_FOUND));
		Member member = memberRepository.findById(paymentRequest.getMemberId())
				.orElseThrow(() -> new CustomException(MemberErrorCode.USER_NOT_FOUND));
		
		Payment payment = Payment.builder()
		.orderId(paymentRequest.getOrderId())
		.price(paymentRequest.getAmount())
		.member(member)
		.credit(credit)
		.method(paymentRequest.getMethod())
		.build();
		paymentRepository.save(payment);
		
		return payment.getPaymentId();
	}

	@Override
	@Transactional
	public Long verify(String paymentKey, String orderId, Long price) {
		Payment payment = paymentRepository.findByOrderId(orderId)
		.orElseThrow(() -> new CustomException(PaymentErrorCode.PAYMENT_NOT_FOUND));
		
		if(payment.getPrice().equals(price)) {
			payment.setPaymentKey(paymentKey);
			return payment.getPaymentId();
		} else {
			throw new CustomException(PaymentErrorCode.PAYMENT_AMOUNT_ERROR);
		}
	}

	@Override
	@Transactional
	public PaymentConfirmResponse requestConfirm(String paymentKey, String orderId, Long price) throws Exception {
		RestTemplate rest = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		
		String authorization = new String(Base64.getEncoder().encode((secretKey + ":").getBytes(StandardCharsets.UTF_8)));
		headers.setBasicAuth(authorization);
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		
	    JSONObject obj = new JSONObject();
	    obj.put("orderId", orderId);
	    obj.put("amount", String.valueOf(price));
	    obj.put("paymentKey", paymentKey);
        
        PaymentConfirmResponse response = rest.postForEntity("https://api.tosspayments.com/v1/payments/confirm", new HttpEntity<>(obj, headers), PaymentConfirmResponse.class).getBody();
        
        Payment payment = paymentRepository.findByOrderId(orderId)
        	.orElseThrow(() -> new CustomException(PaymentErrorCode.PAYMENT_NOT_FOUND));
        payment.changeState(response.getStatus());
        
        return response;
	}
}
