package com.hifive.hiapp.domain.member.service;

import com.hifive.hiapp.domain.member.dto.SignupRequest;
import com.hifive.hiapp.domain.member.dto.TokenDTO;

public interface IMemberService {
	TokenDTO login(String email, String password);
	Long signup(SignupRequest signupRequest);
}