package com.hifive.bururung.domain.payment.entity;

import static jakarta.persistence.FetchType.LAZY;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.hifive.bururung.domain.credit.entity.Credit;
import com.hifive.bururung.domain.member.entity.Member;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {
	
	@Id
	@SequenceGenerator(
			name = "PAYMENT_SEQ_GEN",
			sequenceName = "PAYMENT_SEQ",
			allocationSize = 1
	)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAYMENT_SEQ_GEN")
	private Long paymentId;
	
	private String paymentKey;
	
	private String orderId;
	
	private Long price;
	
	@Enumerated(EnumType.STRING)
	private PaymentMethod method;
	
	@Enumerated(EnumType.STRING)
	private PaymentState state;
	
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
	private Member member;
    
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "credit_id")
    private Credit credit;
	
	private LocalDateTime createdDate;
	
	private LocalDateTime approvedDate;

	@Builder
	public Payment(PaymentMethod method, String orderId, Long price, Credit credit, Member member) {
		this.method = method;
		this.orderId = orderId;
		this.price = price;
		this.credit = credit;
		this.member = member;
		this.state = PaymentState.IN_PROGRESS;
		this.createdDate = LocalDateTime.now();
	}
	
	public void setPaymentKey(String paymentKey) {
		this.paymentKey = paymentKey;
	}
	
	public void changeState(String state) {
		if(state.equals("DONE")) {
			this.state = PaymentState.DONE;
		} else {
			this.state = PaymentState.ABORTED;
		}
		approvedDate = LocalDateTime.now();
	}
}
