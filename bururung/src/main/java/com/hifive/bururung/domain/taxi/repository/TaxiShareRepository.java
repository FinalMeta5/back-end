package com.hifive.bururung.domain.taxi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hifive.bururung.domain.taxi.entity.TaxiShareEntity;

import jakarta.persistence.LockModeType;

public interface TaxiShareRepository extends JpaRepository<TaxiShareEntity, Long>{
	
	@Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
	@Query("select t from TaxiShareEntity t where t.taxiShareId = :id")
	Optional<TaxiShareEntity> findByIdWithLock(@Param("id") Long id);
}
