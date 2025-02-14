package com.hifive.bururung.domain.carshare.organizer.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.hifive.bururung.domain.carshare.organizer.entity.CarShareRegistration;

import jakarta.transaction.Transactional;

public interface CarShareRepository extends JpaRepository<CarShareRegistration, Long>{
	List<CarShareRegistration> findByMemberId(Long memberId);
}
