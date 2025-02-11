package com.hifive.bururung.domain.credit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hifive.bururung.domain.credit.entity.MemberCredit;

public interface MemberCreditRepository extends JpaRepository<MemberCredit, Long>{

}
