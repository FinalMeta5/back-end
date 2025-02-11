package com.hifive.bururung.domain.credit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hifive.bururung.domain.credit.entity.Credit;

public interface CreditRepository extends JpaRepository<Credit, Long> {

	Optional<Credit> findByCount(int count);
}
