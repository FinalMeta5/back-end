package com.hifive.bururung.domain.member.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.hifive.bururung.domain.member.entity.Member;
import com.hifive.bururung.domain.member.repository.MemberRepository;
import com.hifive.bururung.global.common.s3.S3Uploader;
import com.hifive.bururung.global.common.s3.UploadFileDTO;
import com.hifive.bururung.global.exception.CustomException;
import com.hifive.bururung.global.exception.errorcode.MemberErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MypageService implements IMypageService {
	
	private final S3Uploader s3Uploader;
	private final MemberRepository memberRepository;
	
	@Transactional
	public String uploadProfile(MultipartFile multipartFile, String subpath, Long memberId) throws IOException {
		Member member = memberRepository.findById(memberId)
		.orElseThrow(() -> new CustomException(MemberErrorCode.USER_NOT_FOUND));
		
		if(member.getImageUrl() != null && StringUtils.hasText(member.getImageUrl())) {
			s3Uploader.deleteFile(subpath + member.getImageName());
		}
		
		UploadFileDTO uploadFileDTO = s3Uploader.uploadFile(multipartFile, subpath);
		member.changeProfile(uploadFileDTO.getStoreFileName(), uploadFileDTO.getStoreFullUrl());
		
		return uploadFileDTO.getStoreFullUrl();
	}
	
}
