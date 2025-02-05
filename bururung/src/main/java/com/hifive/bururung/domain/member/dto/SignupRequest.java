package com.hifive.bururung.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
	
	private String email;
	
	private String password;
	
	private String phone;
	
	private String name;
	
	private Character gender;
	
	private String birth;
	
	private String nickname;
}
