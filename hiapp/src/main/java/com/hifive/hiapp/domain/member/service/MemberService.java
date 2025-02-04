package com.hifive.hiapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hifive.hiapp.dao.IMemberRepository;
import com.hifive.hiapp.dto.MemberDTO;

@Service
public class MemberService implements IMemberService{
	@Autowired
	IMemberRepository memberRepository;

	@Override
	public List<MemberDTO> findAll() {
		return memberRepository.findAll();
	}

	@Override
	public int getMemberCount() {
		return memberRepository.getMemberCount();
	}

	@Override
	public MemberDTO getMemberInfo(int memberId) {
		return memberRepository.getMemberInfo(memberId);
	}
	
}