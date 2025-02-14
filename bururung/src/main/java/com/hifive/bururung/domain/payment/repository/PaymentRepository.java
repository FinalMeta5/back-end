package com.hifive.bururung.domain.payment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hifive.bururung.domain.payment.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
	Optional<Payment> findByOrderId(String orderId);
	
	@Query("select c.count from Payment p join p.credit c where p.paymentId = :id")
	int findCreditCount(@Param("id") Long paymentId);
}
