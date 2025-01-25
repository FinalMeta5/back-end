package com.hifive.hiapp.service;

import java.util.List;

import com.hifive.hiapp.dto.MemberDTO;

public interface IMemberService {
	List<MemberDTO> findAll();
	int getMemberCount();
	MemberDTO getMemberInfo(int memberId);
}