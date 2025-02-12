package com.hifive.bururung.domain.member.dto;

import com.hifive.bururung.domain.credit.entity.MemberCreditState;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreditHistoryResponse {
	private Long memberCreditId;
	private String date;
	private Integer creditCount;
	private String state;
	
	public CreditHistoryResponse(CreditHistoryDTO creditHistoryDTO) {
		this.memberCreditId = creditHistoryDTO.getMemberCreditId();
		String[] split = creditHistoryDTO.getDate().split("-");
		this.date = split[0] + "년 " + split[1] + "월 " + split[2] + "일";
		this.creditCount = creditHistoryDTO.getCreditCount();
		this.state = creditHistoryDTO.getState().getValue();
	}
}
