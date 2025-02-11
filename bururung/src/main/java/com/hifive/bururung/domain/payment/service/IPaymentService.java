package com.hifive.bururung.domain.payment.service;

import com.hifive.bururung.domain.payment.dto.PaymentConfirmResponse;
import com.hifive.bururung.domain.payment.dto.PaymentRequest;

public interface IPaymentService {
	Long requestPayment(PaymentRequest paymentRequest);
	Long verify(String paymentKey, String orderId, Long price);
	PaymentConfirmResponse requestConfirm(String paymentKey, String orderId, Long price) throws Exception;
}
