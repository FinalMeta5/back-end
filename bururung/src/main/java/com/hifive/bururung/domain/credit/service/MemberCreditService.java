package com.hifive.bururung.domain.credit.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hifive.bururung.domain.credit.entity.MemberCredit;
import com.hifive.bururung.domain.credit.entity.MemberCreditState;
import com.hifive.bururung.domain.credit.repository.MemberCreditRepository;
import com.hifive.bururung.domain.member.entity.Member;
import com.hifive.bururung.domain.member.repository.MemberRepository;
import com.hifive.bururung.global.exception.CustomException;
import com.hifive.bururung.global.exception.errorcode.MemberErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberCreditService implements IMemberCreditService {

	private final MemberRepository memberRepository;
	private final MemberCreditRepository memberCreditRepository;
	
	@Override
	@Transactional
	public void chargeCredit(Long memberId, int creditCount) {
		Member member = memberRepository.findById(memberId)
		.orElseThrow(() -> new CustomException(MemberErrorCode.USER_NOT_FOUND));
		member.changeCredit(creditCount);
		
		MemberCredit memberCredit = MemberCredit.builder()
		.member(member)
		.state(MemberCreditState.CHARGE)
		.count(creditCount)
		.build();
		
		memberCreditRepository.save(memberCredit);
	}
	
}
