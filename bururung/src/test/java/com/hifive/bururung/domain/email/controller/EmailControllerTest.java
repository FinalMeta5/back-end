package com.hifive.bururung.domain.email.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.hifive.bururung.domain.email.dto.EmailCheckRequest;
import com.hifive.bururung.domain.email.dto.EmailSendRequest;
import com.hifive.bururung.domain.email.dto.EmailType;
import com.hifive.bururung.domain.email.service.EmailService;

import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
public class EmailControllerTest {

    @InjectMocks
    private EmailController emailController;
    
    @Mock
    private EmailService emailService;
    
    @Mock
    private EmailSendRequest emailSendRequest;
    
    @Mock
    private EmailCheckRequest emailCheckRequest;
    
    @Mock
    private HttpSession httpSession;
    
    @Test
    void testSendSignupMail() {
        String email = "test@example.com";
        String code = "123456";
        when(emailSendRequest.getEmail()).thenReturn(email);
        when(emailService.createCode(email)).thenReturn(code);
        
        ResponseEntity<?> response = emailController.sendSignupMail(emailSendRequest);
        
        verify(emailService).createCode(email);
        verify(emailService).sendAuthenticationMail(EmailType.SIGNUP, email, code);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    
    @Test
    void testSendChangePasswordMail() {
        String email = "test@example.com";
        String code = "654321";
        when(emailSendRequest.getEmail()).thenReturn(email);
        when(emailService.createCode(email)).thenReturn(code);
        
        ResponseEntity<?> response = emailController.sendChangePasswordMail(httpSession, emailSendRequest);
        
        verify(emailService).createCode(email);
        verify(emailService).sendAuthenticationMail(EmailType.CHANGE_PASSWORD, email, code);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    
    @Test
    void testCheckSignupAuthentication() {
        String email = "test@example.com";
        String code = "abcdef";
        when(emailCheckRequest.getEmail()).thenReturn(email);
        when(emailCheckRequest.getCode()).thenReturn(code);
        when(emailService.isAuthenticated(email, code)).thenReturn(true);
        
        ResponseEntity<Boolean> response = emailController.checkSignupAuthentication(httpSession, emailCheckRequest);
        
        verify(emailService).isAuthenticated(email, code);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }
}


