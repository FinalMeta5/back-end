package com.hifive.bururung.domain.member.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import com.hifive.bururung.domain.member.dto.ChangePasswordRequest;
import com.hifive.bururung.domain.member.dto.FindEmailRequest;
import com.hifive.bururung.domain.member.dto.LoginRequest;
import com.hifive.bururung.domain.member.dto.LoginResponse;
import com.hifive.bururung.domain.member.dto.SignupRequest;
import com.hifive.bururung.domain.member.dto.TokenDTO;
import com.hifive.bururung.domain.member.service.IMemberService;
import com.hifive.bururung.global.common.CookieUtil;

import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
public class MemberControllerTest {

    @InjectMocks
    private MemberController memberController;

    @Mock
    private IMemberService memberService;
    
    @Mock
    private CookieUtil cookieUtil;
    
    @Mock
    private HttpServletResponse response;
    
    @BeforeEach
    void setUp() {
        // refreshTokenValidity는 주입되는 값이므로 테스트 환경에서 임의의 값(예: 3600초)으로 설정합니다.
        ReflectionTestUtils.setField(memberController, "refreshTokenValidity", 3600L);
    }
    
    // 1. POST /api/member/login
    @Test
    void testLogin_Success() {
        // given
        String email = "user@example.com";
        String password = "password123";
        
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);
        
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setAccessToken("access-token");
        tokenDTO.setRefreshToken("refresh-token");
        
        LoginResponse loginResponse = new LoginResponse();
        // 필요에 따라 loginResponse 필드값 설정
        
        when(memberService.login(email, password)).thenReturn(tokenDTO);
        when(memberService.getLoginResponse(email)).thenReturn(loginResponse);
        
        // when
        ResponseEntity<LoginResponse> responseEntity = memberController.login(response, loginRequest);
        
        // then
        // setAccessToken() 내부: response.setHeader("accesstoken", accessToken) 호출 검증
        verify(response).setHeader("accesstoken", "access-token");
        // setRefreshToken() 내부: cookieUtil.addCookie(response, "refreshtoken", refreshToken, refreshTokenValidity) 호출 검증
        verify(cookieUtil).addCookie(response, "refreshtoken", "refresh-token", 3600);
        
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(loginResponse, responseEntity.getBody());
    }
    
    // 2. POST /api/member/signup
    @Test
    void testSignup_Success() {
        // given
        SignupRequest signupRequest = new SignupRequest();
        // 필요한 필드 설정 (예: 이메일, 이름 등)
        
        Long memberId = 1L;
        when(memberService.signup(signupRequest)).thenReturn(memberId);
        
        // when
        ResponseEntity<Long> responseEntity = memberController.signup(signupRequest);
        
        // then
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(memberId, responseEntity.getBody());
    }
    
    // 3. POST /api/member/find-email
    @Test
    void testFindEmail_Success() {
        // given
        FindEmailRequest findEmailRequest = new FindEmailRequest();
        findEmailRequest.setName("John Doe");
        findEmailRequest.setPhone("01012345678");
        
        String foundEmail = "john.doe@example.com";
        when(memberService.findEmail("John Doe", "01012345678")).thenReturn(foundEmail);
        
        // when
        ResponseEntity<String> responseEntity = memberController.findId(findEmailRequest);
        
        // then
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(foundEmail, responseEntity.getBody());
    }
    
    // 4. POST /api/member/change-password
    @Test
    void testChangePassword_Success() {
        // given
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setEmail("user@example.com");
        changePasswordRequest.setPassword("newPassword123");
        
        Long changedId = 1L;
        when(memberService.changePassword("user@example.com", "newPassword123")).thenReturn(changedId);
        
        // when
        ResponseEntity<Long> responseEntity = memberController.changePassword(changePasswordRequest);
        
        // then
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(changedId, responseEntity.getBody());
    }
}
