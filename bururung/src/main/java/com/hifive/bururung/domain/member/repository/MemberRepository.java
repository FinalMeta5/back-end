package com.hifive.bururung.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hifive.bururung.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long>{
	Optional<Member> findByEmail(String email);
	
    @Query(value = "select m.email from Member m where m.name = :name and phone = :phone")
    Optional<String> findEmailByNameAndPhone(@Param("name") String name, @Param("phone") String phone);
}
