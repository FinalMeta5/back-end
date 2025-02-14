package com.hifive.bururung.domain.payment.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;

import com.hifive.bururung.domain.credit.service.IMemberCreditService;
import com.hifive.bururung.domain.payment.dto.ConfirmRequest;
import com.hifive.bururung.domain.payment.dto.PaymentConfirmResponse;
import com.hifive.bururung.domain.payment.dto.PaymentRequest;
import com.hifive.bururung.domain.payment.repository.PaymentRepository;
import com.hifive.bururung.domain.payment.service.IPaymentService;

@ExtendWith(MockitoExtension.class)
public class PaymentControllerTest {

    @InjectMocks
    private PaymentController paymentController;
    
    @Mock
    private IPaymentService paymentService;
    
    @Mock
    private IMemberCreditService memberCreditService;
    
    @Mock
    private PaymentRepository paymentRepository;
    
    @Mock
    private PaymentRequest paymentRequest;
    
    @Mock
    private ConfirmRequest confirmRequest;
    
    // @AuthenticationPrincipal로 주입될 User 객체를 @Mock으로 생성
    @Mock
    private User user;
    
    // 1. POST /api/payment/request 테스트
    @Test
    void testRequestPayment_Success() {
        Long paymentId = 100L;
        when(paymentService.requestPayment(paymentRequest)).thenReturn(paymentId);
        
        ResponseEntity<Long> response = paymentController.requestPayment(paymentRequest);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(paymentId, response.getBody());
        verify(paymentService).requestPayment(paymentRequest);
    }
    
    // 2. POST /api/payment/confirm 테스트 (정상 케이스)
    @Test
    void testConfirmPayment_Success() throws Exception {
        // User의 username을 "1"로 stubbing하여 memberId = 1L로 변환
        when(user.getUsername()).thenReturn("1");
        
        // ConfirmRequest의 stubbing
        String paymentKey = "paymentKey123";
        String orderId = "order456";
        int price = 1000;
        when(confirmRequest.getPaymentKey()).thenReturn(paymentKey);
        when(confirmRequest.getOrderId()).thenReturn(orderId);
        when(confirmRequest.getPrice()).thenReturn((long) price);
        
        // paymentService.verify()가 paymentId를 반환하도록 stubbing
        Long paymentId = 200L;
        when(paymentService.verify(paymentKey, orderId, (long) price)).thenReturn(paymentId);
        
        // paymentService.requestConfirm()가 PaymentConfirmResponse를 반환하도록 stubbing
        PaymentConfirmResponse paymentConfirmResponse = mock(PaymentConfirmResponse.class);
        when(paymentService.requestConfirm(paymentKey, orderId, (long) price)).thenReturn(paymentConfirmResponse);
        
        // paymentRepository.findCreditCount() stubbing
        int creditCount = 50;
        when(paymentRepository.findCreditCount(paymentId)).thenReturn(creditCount);
        
        // Call confirmPayment
        ResponseEntity<PaymentConfirmResponse> response = paymentController.confirmPayment(user, confirmRequest);
        
        // Verify memberCreditService.chargeCredit() 호출 (memberId = 1, creditCount = 50)
        verify(memberCreditService).chargeCredit(1L, creditCount);
        
        // Assert ResponseEntity
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(paymentConfirmResponse, response.getBody());
    }
}
