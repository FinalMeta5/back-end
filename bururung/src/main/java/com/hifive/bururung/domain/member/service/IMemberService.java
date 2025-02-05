package com.hifive.bururung.domain.member.service;

import com.hifive.bururung.domain.member.dto.SignupRequest;
import com.hifive.bururung.domain.member.dto.TokenDTO;

public interface IMemberService {
	TokenDTO login(String email, String password);
	Long signup(SignupRequest signupRequest);
}