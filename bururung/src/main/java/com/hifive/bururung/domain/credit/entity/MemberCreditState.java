package com.hifive.bururung.domain.credit.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MemberCreditState {
	PAY("지급"),// 지급
	CHARGE("충전"), // 충전
	TEXI("택시이용"), // 택시 이용
	CAR("차량이용"); // 차량공유 이용
	
	private final String value;
}
