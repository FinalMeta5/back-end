package com.hifive.bururung.domain.credit.entity;

import java.time.LocalDateTime;

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
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import static jakarta.persistence.FetchType.*;

@Entity
@Table(name = "MEMBER_CREDIT")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberCredit {

	@Id
	@SequenceGenerator(
			name = "MEMBER_CREDIT_SEQ_GEN",
			sequenceName = "MEMBER_CREDIT_SEQ",
			allocationSize = 1
	)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_CREDIT_SEQ_GEN")
	private Long memberCreditId;
	
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
	private Member member;
	
	@Enumerated(EnumType.STRING)
    private MemberCreditState state;
	
	private Integer count;
	
	private LocalDateTime transactionDate;

	@Builder
	public MemberCredit(Member member, MemberCreditState state, Integer count) {
		this.member = member;
		this.state = state;
		this.count = count;
		this.transactionDate = LocalDateTime.now();
	}
	
	
}
