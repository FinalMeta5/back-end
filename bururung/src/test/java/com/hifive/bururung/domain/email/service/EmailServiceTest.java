package com.hifive.bururung.domain.email.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import com.hifive.bururung.domain.email.dto.EmailType;
import com.hifive.bururung.global.common.RedisUtil;
import com.hifive.bururung.global.exception.CustomException;
import com.hifive.bururung.global.exception.errorcode.MemberErrorCode;

import jakarta.mail.internet.MimeMessage;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;
    
    @Mock
    private JavaMailSender javaMailSender;
    
    @Mock
    private RedisUtil redisUtil;
    
    @BeforeEach
    void setUp() {
        // "id"는 @Value("${spring.mail.username}")로 주입되므로, 테스트에서는 모의값 "test"로 설정합니다.
        org.springframework.test.util.ReflectionTestUtils.setField(emailService, "id", "test");
    }
    
    // createCode(String email) 테스트
    @Test
    void testCreateCode() {
        String email = "test@example.com";
        // 호출 시 랜덤 6자리 코드가 반환됨
        String code = emailService.createCode(email);
        assertNotNull(code);
        assertEquals(6, code.length());
        
        // redisUtil.setData()가 호출되었는지 검증
        String expectedKey = "EMAIL_AUTH:" + email;
        ArgumentCaptor<Duration> durationCaptor = ArgumentCaptor.forClass(Duration.class);
        verify(redisUtil).setData(eq(expectedKey), eq(code), durationCaptor.capture());
        assertEquals(Duration.ofSeconds(300), durationCaptor.getValue());
    }
    
    // sendAuthenticationMail() 테스트 - EmailType.SIGNUP
    @Test
    void testSendAuthenticationMail_Signup() throws Exception {
        String email = "test@example.com";
        String code = "ABCDEF";
        
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        
        // 호출
        emailService.sendAuthenticationMail(EmailType.SIGNUP, email, code);
        
        // 메일 전송을 위해 javaMailSender.send()가 호출되었는지 검증
        verify(javaMailSender).send(mimeMessage);
    }
    
    // sendAuthenticationMail() 테스트 - EmailType.CHANGE_PASSWORD
    @Test
    void testSendAuthenticationMail_ChangePassword() throws Exception {
        String email = "test@example.com";
        String code = "XYZ123";
        
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        
        emailService.sendAuthenticationMail(EmailType.CHANGE_PASSWORD, email, code);
        
        verify(javaMailSender).send(mimeMessage);
    }
    
    // isAuthenticated() 테스트 - 성공 케이스
    @Test
    void testIsAuthenticated_Success() {
        String email = "test@example.com";
        String code = "ABCDEF";
        
        when(redisUtil.getData("EMAIL_AUTH:" + email)).thenReturn(code);
        
        boolean result = emailService.isAuthenticated(email, code);
        assertTrue(result);
    }
    
    // isAuthenticated() 테스트 - 실패 케이스
    @Test
    void testIsAuthenticated_Failure() {
        String email = "test@example.com";
        String code = "ABCDEF";
        
        when(redisUtil.getData("EMAIL_AUTH:" + email)).thenReturn("WRONG");
        
        CustomException exception = assertThrows(CustomException.class, () ->
            emailService.isAuthenticated(email, code)
        );
        assertEquals(MemberErrorCode.EMAIL_AUTH_FAIL, exception.getErrorCode());
    }
}


