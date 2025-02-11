package com.hifive.bururung.domain.credit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Credit {
	
	@Id
	@SequenceGenerator(
			name = "CREDIT_SEQ_GEN",
			sequenceName = "CREDIT_SEQ",
			allocationSize = 1
	)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CREDIT_SEQ_GEN")
	private Long creditId;
	
	private Integer count;
	
	private Integer price;
}
