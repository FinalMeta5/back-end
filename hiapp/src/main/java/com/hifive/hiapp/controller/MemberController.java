package com.hifive.hiapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hifive.hiapp.dto.MemberDTO;
import com.hifive.hiapp.service.IMemberService;

@RestController
@RequestMapping("/member")
public class MemberController {
	@Autowired 
	IMemberService memberService;
	
	@GetMapping("/all")
	public List<MemberDTO> findAll(){
		return memberService.findAll();
	}
	
	@GetMapping("/count")
	public int getMemberCount() {
		return memberService.getMemberCount();
	}
	
	@GetMapping("/{memberId}")
	public MemberDTO getMemberInfo(@PathVariable("memberId") int memberId) {
		return memberService.getMemberInfo(memberId);
	}
}