package com.hifive.bururung.domain.carshare.organizer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hifive.bururung.domain.carshare.organizer.entity.CarShareRegistration;

public interface CarShareRepository extends JpaRepository<CarShareRegistration, Long>{
	List<CarShareRegistration> findByMemberId(Long memberId);
}
