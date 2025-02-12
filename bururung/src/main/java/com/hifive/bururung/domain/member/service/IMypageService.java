package com.hifive.bururung.domain.member.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hifive.bururung.domain.member.dto.CreditHistoryResponse;
import com.hifive.bururung.domain.member.dto.MypageResponse;

public interface IMypageService {
	String uploadProfile(MultipartFile multipartFile, String subpath, Long memberId) throws IOException;
	List<CreditHistoryResponse> getCreditHistory(Long memberId, int year, int month);
	Integer getCreditBalance(Long memberId);
	MypageResponse getMypage(Long memberId);
}
