package com.hifive.bururung.domain.carshare.organizer.repository;


import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hifive.bururung.domain.carshare.organizer.entity.CarRegistration;

public interface CarRegistrationRepository extends JpaRepository<CarRegistration, Long>{
	Optional<CarRegistration> findByMember_MemberId(Long memberId);
	Optional<CarRegistration> findByCarNumber(String carNumber);
	@Query("SELECT COUNT(c) > 0 FROM CarRegistration c WHERE c.member.memberId = :memberId")
	boolean existsByMember_MemberId(@Param("memberId") Long memberId);

}
