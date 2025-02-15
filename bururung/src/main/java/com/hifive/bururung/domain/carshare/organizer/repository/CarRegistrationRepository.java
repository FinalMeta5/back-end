package com.hifive.bururung.domain.carshare.organizer.repository;


import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hifive.bururung.domain.carshare.organizer.entity.CarRegistration;
import com.hifive.bururung.domain.taxi.entity.TaxiShareEntity;

import jakarta.persistence.LockModeType;

public interface CarRegistrationRepository extends JpaRepository<CarRegistration, Long>{
	Optional<CarRegistration> findByMember_MemberId(Long memberId);
	Optional<CarRegistration> findByCarNumber(String carNumber);
	@Query("SELECT COUNT(c) > 0 FROM CarRegistration c WHERE c.member.memberId = :memberId")
	boolean existsByMember_MemberId(@Param("memberId") Long memberId);
	@Query("SELECT c.verified FROM CarRegistration c WHERE c.member.memberId = :memberId")
    String findVerifiedByMemberId(@Param("memberId") Long memberId);
	@Query("SELECT COUNT(c) FROM CarRegistration c WHERE c.carNumber = :carNumber")
	Long countByCarNumber(@Param("carNumber") String carNumber);
	@Query("SELECT c.carId FROM CarRegistration c WHERE c.member.memberId = :memberId")
	Long findCarIdByMemberId(@Param("memberId") Long memberId);

	@Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
	@Query("select c from CarRegistration c where c.carId = :id")
	Optional<CarRegistration> findByIdWithLock(@Param("id") Long id);
}
