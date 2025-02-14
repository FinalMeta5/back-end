package com.hifive.bururung.domain.payment.service;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.hifive.bururung.domain.credit.entity.Credit;
import com.hifive.bururung.domain.credit.repository.CreditRepository;
import com.hifive.bururung.domain.member.entity.Member;
import com.hifive.bururung.domain.member.repository.MemberRepository;
import com.hifive.bururung.domain.payment.dto.PaymentConfirmResponse;
import com.hifive.bururung.domain.payment.dto.PaymentRequest;
import com.hifive.bururung.domain.payment.entity.Payment;
import com.hifive.bururung.domain.payment.entity.PaymentMethod;
import com.hifive.bururung.domain.payment.repository.PaymentRepository;
import com.hifive.bururung.global.exception.CustomException;
import com.hifive.bururung.global.exception.errorcode.MemberErrorCode;
import com.hifive.bururung.global.exception.errorcode.PaymentErrorCode;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;
    
    @Mock
    private PaymentRepository paymentRepository;
    
    @Mock
    private CreditRepository creditRepository;
    
    @Mock
    private MemberRepository memberRepository;
    
    // secretKey는 @Value로 주입되므로, 테스트에서 ReflectionTestUtils로 주입
    @BeforeEach
    void setUp() {
        org.springframework.test.util.ReflectionTestUtils.setField(paymentService, "secretKey", "secret");
    }
    
    // --- requestPayment() ---
    
    @Test
    void testRequestPayment_Success() {
        // PaymentRequest를 @Mock으로 생성
        PaymentRequest paymentRequest = mock(PaymentRequest.class);
        when(paymentRequest.getCreditCount()).thenReturn(100);
        when(paymentRequest.getMemberId()).thenReturn(1L);
        when(paymentRequest.getOrderId()).thenReturn("order123");
        when(paymentRequest.getAmount()).thenReturn(5000L);
        when(paymentRequest.getMethod()).thenReturn(PaymentMethod.CARD);
        
        // Credit와 Member 도메인 객체를 @Mock으로 생성
        Credit creditMock = mock(Credit.class);
        Member memberMock = mock(Member.class);
        
        when(creditRepository.findByCount(100)).thenReturn(Optional.of(creditMock));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(memberMock));
        
        // Payment 객체는 builder() 내부에서 생성되므로, save() 메서드에서 전달받은 객체를 반환하도록 stubbing.
        // Payment는 도메인 객체이므로 @Spy를 사용하여 생성.
        Payment paymentSpy = mock(Payment.class, withSettings().defaultAnswer(org.mockito.Answers.CALLS_REAL_METHODS));
        // stubbing getPaymentId() to return 10L
        when(paymentSpy.getPaymentId()).thenReturn(10L);
        when(paymentRepository.save(any(Payment.class))).thenReturn(paymentSpy);
        
        Long paymentId = paymentService.requestPayment(paymentRequest);
        assertEquals(10L, paymentId);
        
        // Verify Payment 객체의 필드가 올바르게 설정되었는지 캡처
        ArgumentCaptor<Payment> captor = ArgumentCaptor.forClass(Payment.class);
        verify(paymentRepository).save(captor.capture());
        Payment savedPayment = captor.getValue();
        assertEquals("order123", savedPayment.getOrderId());
        assertEquals(5000L, savedPayment.getPrice());
        assertEquals("CARD", savedPayment.getMethod());
        assertEquals(memberMock, savedPayment.getMember());
        assertEquals(creditMock, savedPayment.getCredit());
    }
    
    @Test
    void testRequestPayment_CreditNotFound() {
        PaymentRequest paymentRequest = mock(PaymentRequest.class);
        when(paymentRequest.getCreditCount()).thenReturn(100);
        when(creditRepository.findByCount(100)).thenReturn(Optional.empty());
        
        CustomException exception = assertThrows(CustomException.class, () -> 
            paymentService.requestPayment(paymentRequest)
        );
        assertEquals(PaymentErrorCode.CREDIT_NOT_FOUND, exception.getErrorCode());
    }
    
    @Test
    void testRequestPayment_MemberNotFound() {
        PaymentRequest paymentRequest = mock(PaymentRequest.class);
        when(paymentRequest.getCreditCount()).thenReturn(100);
        Credit creditMock = mock(Credit.class);
        when(creditRepository.findByCount(100)).thenReturn(Optional.of(creditMock));
        when(paymentRequest.getMemberId()).thenReturn(1L);
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());
        
        CustomException exception = assertThrows(CustomException.class, () ->
            paymentService.requestPayment(paymentRequest)
        );
        assertEquals(MemberErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }
    
    // --- verify() ---
    
    @Test
    void testVerify_Success() {
        String orderId = "order123";
        Long price = 5000L;
        String paymentKey = "payKey123";
        
        Payment paymentSpy = mock(Payment.class, withSettings().defaultAnswer(org.mockito.Answers.CALLS_REAL_METHODS));
        when(paymentSpy.getPrice()).thenReturn(price);
        when(paymentSpy.getPaymentId()).thenReturn(20L);
        when(paymentRepository.findByOrderId(orderId)).thenReturn(Optional.of(paymentSpy));
        
        Long resultPaymentId = paymentService.verify(paymentKey, orderId, price);
        assertEquals(20L, resultPaymentId);
        verify(paymentSpy).setPaymentKey(paymentKey);
    }
    
    @Test
    void testVerify_PaymentAmountError() {
        String orderId = "order123";
        Long expectedPrice = 5000L;
        Long wrongPrice = 4000L;
        String paymentKey = "payKey123";
        
        Payment paymentMock = mock(Payment.class);
        when(paymentMock.getPrice()).thenReturn(expectedPrice);
        when(paymentRepository.findByOrderId(orderId)).thenReturn(Optional.of(paymentMock));
        
        CustomException exception = assertThrows(CustomException.class, () ->
            paymentService.verify(paymentKey, orderId, wrongPrice)
        );
        assertEquals(PaymentErrorCode.PAYMENT_AMOUNT_ERROR, exception.getErrorCode());
    }
    
    // --- requestConfirm() ---
    
    @Test
    void testRequestConfirm_Success() throws Exception {
        String paymentKey = "payKey123";
        String orderId = "order123";
        Long price = 5000L;
        
        // PaymentConfirmResponse를 @Mock으로 생성
        PaymentConfirmResponse confirmResponseMock = mock(PaymentConfirmResponse.class);
        when(confirmResponseMock.getStatus()).thenReturn("CONFIRMED");
        
        // Payment 객체 (spy)
        Payment paymentSpy = mock(Payment.class, withSettings().defaultAnswer(org.mockito.Answers.CALLS_REAL_METHODS));
        when(paymentRepository.findByOrderId(orderId)).thenReturn(Optional.of(paymentSpy));
        
        // RestTemplate 생성 시 new RestTemplate()을 모의하기 위해 mockConstruction 사용
        try (MockedConstruction<RestTemplate> restTemplateMocked = mockConstruction(RestTemplate.class, (mock, context) -> {
            when(mock.postForEntity(anyString(), any(HttpEntity.class), eq(PaymentConfirmResponse.class)))
                .thenReturn(ResponseEntity.ok(confirmResponseMock));
        })) {
            PaymentConfirmResponse result = paymentService.requestConfirm(paymentKey, orderId, price);
            // verify that payment.changeState() was called with "CONFIRMED"
            verify(paymentSpy).changeState("CONFIRMED");
            assertEquals(confirmResponseMock, result);
            
            // Optional: 캡처하여 HttpEntity 내부의 JSON 객체나 헤더를 검증 가능 (생략)
        }
    }
    
    @Test
    void testRequestConfirm_PaymentNotFound() {
        String paymentKey = "payKey123";
        String orderId = "order123";
        Long price = 5000L;
        
        PaymentConfirmResponse confirmResponseMock = mock(PaymentConfirmResponse.class);
        when(confirmResponseMock.getStatus()).thenReturn("CONFIRMED");
        
        try (MockedConstruction<RestTemplate> restTemplateMocked = mockConstruction(RestTemplate.class, (mock, context) -> {
            when(mock.postForEntity(anyString(), any(HttpEntity.class), eq(PaymentConfirmResponse.class)))
                .thenReturn(ResponseEntity.ok(confirmResponseMock));
        })) {
            when(paymentRepository.findByOrderId(orderId)).thenReturn(Optional.empty());
            CustomException exception = assertThrows(CustomException.class, () ->
                paymentService.requestConfirm(paymentKey, orderId, price)
            );
            assertEquals(PaymentErrorCode.PAYMENT_NOT_FOUND, exception.getErrorCode());
        }
    }
}
