package com.hifive.bururung.domain.member.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.hifive.bururung.domain.member.dto.CreditHistoryDTO;
import com.hifive.bururung.domain.member.dto.MypageResponse;

@Repository
@Mapper
public interface MypageMapper {
	List<CreditHistoryDTO> getCreditHistoryByDate(
	        @Param("memberId") Long memberId, 
	        @Param("year") int year, 
	        @Param("month") int month);
	
	MypageResponse getMypage(@Param("memberId") Long memberId);
}
