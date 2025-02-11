package com.hifive.bururung.domain.credit.service;

public interface IMemberCreditService {
	void chargeCredit(Long memberId, int creditCount);
}
