package com.hifive.bururung.domain.member.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import com.hifive.bururung.domain.member.dto.CreditHistoryDTO;
import com.hifive.bururung.domain.member.dto.CreditHistoryResponse;
import com.hifive.bururung.domain.member.entity.Member;
import com.hifive.bururung.domain.member.repository.MemberRepository;
import com.hifive.bururung.domain.member.repository.MypageMapper;
import com.hifive.bururung.global.common.s3.S3Uploader;
import com.hifive.bururung.global.common.s3.UploadFileDTO;

@ExtendWith(MockitoExtension.class)
public class MypageServiceTest {

    @InjectMocks
    private MypageService mypageService;
    
    @Mock
    private S3Uploader s3Uploader;
    
    @Mock
    private MemberRepository memberRepository;
    
    @Mock
    private MypageMapper mypageMapper;
    
    @Mock
    private MultipartFile multipartFile;
    
    // 도메인 객체는 new 키워드 대신 @Spy를 사용하여 생성 (CALLS_REAL_METHODS로 실제 메서드 호출)
    @Spy
    private Member memberSpy = mock(Member.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
    
    @BeforeEach
    void setUp() {
        // 별도의 설정이 필요하면 여기에 작성 (현재는 특별한 설정 없음)
    }
    
    // 1. uploadProfile: 기존 이미지가 있을 경우 (삭제 호출 확인)
    @Test
    void testUploadProfile_WithExistingImage() throws IOException {
        Long memberId = 1L;
        String subpath = "member/";
        
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(memberSpy));
        // 기존 이미지가 있음: non-empty 값 반환
        when(memberSpy.getImageUrl()).thenReturn("existingUrl");
        when(memberSpy.getImageName()).thenReturn("oldImage.jpg");
        
        // UploadFileDTO를 목으로 생성 (도메인 객체 대신 mock 사용)
        UploadFileDTO uploadFileDTO = mock(UploadFileDTO.class);
        when(uploadFileDTO.getStoreFileName()).thenReturn("newImage.jpg");
        when(uploadFileDTO.getStoreFullUrl()).thenReturn("http://s3.example.com/newImage.jpg");
        
        when(s3Uploader.uploadFile(multipartFile, subpath)).thenReturn(uploadFileDTO);
        
        String resultUrl = mypageService.uploadProfile(multipartFile, subpath, memberId);
        
        // 기존 이미지가 있으므로 삭제가 호출되어야 함.
        verify(s3Uploader).deleteFile(subpath + "oldImage.jpg");
        verify(s3Uploader).uploadFile(multipartFile, subpath);
        verify(memberSpy).changeProfile("newImage.jpg", "http://s3.example.com/newImage.jpg");
        assertEquals("http://s3.example.com/newImage.jpg", resultUrl);
    }
    
    // 2. uploadProfile: 기존 이미지가 없을 경우 (삭제 미호출)
    @Test
    void testUploadProfile_NoExistingImage() throws IOException {
        Long memberId = 1L;
        String subpath = "member/";
        
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(memberSpy));
        // 기존 이미지가 없으므로 빈 문자열 반환
        when(memberSpy.getImageUrl()).thenReturn("");
        
        UploadFileDTO uploadFileDTO = mock(UploadFileDTO.class);
        when(uploadFileDTO.getStoreFileName()).thenReturn("newImage.jpg");
        when(uploadFileDTO.getStoreFullUrl()).thenReturn("http://s3.example.com/newImage.jpg");
        
        when(s3Uploader.uploadFile(multipartFile, subpath)).thenReturn(uploadFileDTO);
        
        String resultUrl = mypageService.uploadProfile(multipartFile, subpath, memberId);
        
        // 삭제 호출 없이 업로드 및 프로필 변경이 이루어져야 함.
        verify(s3Uploader, never()).deleteFile(anyString());
        verify(s3Uploader).uploadFile(multipartFile, subpath);
        verify(memberSpy).changeProfile("newImage.jpg", "http://s3.example.com/newImage.jpg");
        assertEquals("http://s3.example.com/newImage.jpg", resultUrl);
    }
    
    // 3. getCreditHistory
    @Test
    void testGetCreditHistory() {
        Long memberId = 1L;
        int year = 2025;
        int month = 2;
        
        // CreditHistoryDTO 대신 Object를 목으로 사용 (타입 정보가 없으므로)
        CreditHistoryDTO creditHistoryDTO = mock(CreditHistoryDTO.class);
        when(mypageMapper.getCreditHistoryByDate(memberId, year, month))
            .thenReturn(Collections.singletonList(creditHistoryDTO));
        
        List<CreditHistoryResponse> responses = mypageService.getCreditHistory(memberId, year, month);
        // 서비스 내부에서 new CreditHistoryResponse(dto) 호출 – 리스트 크기만 검증
        assertEquals(1, responses.size());
    }
    
    // 4. getCreditBalance
    @Test
    void testGetCreditBalance_Success() {
        Long memberId = 1L;
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(memberSpy));
        when(memberSpy.getCreditCount()).thenReturn(100);
        
        Integer balance = mypageService.getCreditBalance(memberId);
        assertEquals(100, balance);
    }
    
    // 5. getMypage
    @Test
    void testGetMypage_Success() {
        Long memberId = 1L;
        // MypageResponse 도메인 객체를 목으로 생성
        com.hifive.bururung.domain.member.dto.MypageResponse mypageResponse = mock(com.hifive.bururung.domain.member.dto.MypageResponse.class);
        when(mypageMapper.getMypage(memberId)).thenReturn(mypageResponse);
        
        com.hifive.bururung.domain.member.dto.MypageResponse response = mypageService.getMypage(memberId);
        assertEquals(mypageResponse, response);
    }
}
