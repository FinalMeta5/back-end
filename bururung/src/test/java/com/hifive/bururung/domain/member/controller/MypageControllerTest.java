package com.hifive.bururung.domain.member.controller;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.User;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hifive.bururung.domain.member.dto.CreditHistoryResponse;
import com.hifive.bururung.domain.member.dto.MypageResponse;
import com.hifive.bururung.domain.member.service.IMypageService;
import com.hifive.bururung.global.common.s3.FileSubPath;

@ExtendWith(MockitoExtension.class)
public class MypageControllerTest {

    @InjectMocks
    private MypageController mypageController;
    
    @Mock
    private IMypageService mypageService;
    
    // 1. POST /api/mypage/upload-profile
    @Test
    void testUploadProfile_Success() throws IOException {
        // 사용자 아이디를 문자열로 "1"로 설정 (memberId=1L)
        User user = new User("1", "password", List.of());
        // MultipartFile 생성 (프로필 이미지)
        MockMultipartFile profileImage = new MockMultipartFile("profileImage", "test.jpg", "image/jpeg", "dummy content".getBytes(StandardCharsets.UTF_8));
        
        // Service에서 반환할 URL 설정
        String expectedUrl = "http://s3.example.com/profile.jpg";
        when(mypageService.uploadProfile(profileImage, FileSubPath.MEMBER.getPath(), 1L)).thenReturn(expectedUrl);
        
        // Controller 호출
        ResponseEntity<String> response = mypageController.uploadProfile(user, profileImage);
        
        // 검증: HTTP 200 OK와 반환된 URL
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedUrl, response.getBody());
    }
    
    // 2. GET /api/mypage/credit-history?year=&month=
    @Test
    void testGetCreditHistory_Success() {
        User user = new User("1", "password", List.of());
        int year = 2025;
        int month = 2;
        
        // Dummy CreditHistoryResponse 목록 생성
        CreditHistoryResponse history1 = new CreditHistoryResponse();
        CreditHistoryResponse history2 = new CreditHistoryResponse();
        List<CreditHistoryResponse> expectedList = Arrays.asList(history1, history2);
        
        when(mypageService.getCreditHistory(1L, year, month)).thenReturn(expectedList);
        
        ResponseEntity<List<CreditHistoryResponse>> response = mypageController.getCreditHistory(user, year, month);
        
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedList, response.getBody());
    }
    
    // 3. GET /api/mypage/credit/{memberId}
    @Test
    void testGetCreditBalance_Success() {
        Long memberId = 1L;
        int expectedBalance = 100;
        when(mypageService.getCreditBalance(memberId)).thenReturn(expectedBalance);
        
        ResponseEntity<Integer> response = mypageController.getCreditBalance(memberId);
        
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedBalance, response.getBody());
    }
    
    // 4. GET /api/mypage (마이페이지 조회)
    @Test
    void testGetMypage_Success() {
        User user = new User("1", "password", List.of());
        MypageResponse expectedResponse = new MypageResponse();
        when(mypageService.getMypage(1L)).thenReturn(expectedResponse);
        
        ResponseEntity<MypageResponse> response = mypageController.getMypage(user);
        
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse, response.getBody());
    }
}
