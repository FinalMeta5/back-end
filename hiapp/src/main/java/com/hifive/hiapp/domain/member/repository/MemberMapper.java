package com.hifive.hiapp.domain.member.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.hifive.hiapp.domain.member.dto.MemberDTO;

@Repository
@Mapper
public interface MemberMapper {
    // 전체 회원 조회
    List<MemberDTO> findAll();

    // 회원 수 조회
    int getMemberCount();

    // 특정 회원 조회
    MemberDTO getMemberInfo(int memberId);
}