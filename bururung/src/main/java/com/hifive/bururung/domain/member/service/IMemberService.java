package com.hifive.bururung.domain.member.service;

import com.hifive.bururung.domain.member.dto.LoginResponse;
import com.hifive.bururung.domain.member.dto.SignupRequest;
import com.hifive.bururung.domain.member.dto.TokenDTO;
import com.hifive.bururung.domain.member.entity.Member;

public interface IMemberService {
	TokenDTO login(String email, String password);
	Long signup(SignupRequest signupRequest);
	String findEmail(String name, String phone);
	Long changePassword(String email, String password);
	boolean checkMemberExists(Long memberId);
	Member findByMemberId(Long memberId);
	LoginResponse getLoginResponse(String email);
	boolean checkNicknameDuplicated(String nickname);
}