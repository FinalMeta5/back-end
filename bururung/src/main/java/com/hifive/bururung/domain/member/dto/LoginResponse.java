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
public class LoginResponse {
	private Long memberId;
	
	private String nickname;
	
	private String role;
}
