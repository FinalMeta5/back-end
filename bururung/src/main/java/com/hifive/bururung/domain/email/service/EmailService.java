package com.hifive.bururung.domain.email.service;

import java.time.Duration;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.hifive.bururung.domain.email.dto.EmailType;
import com.hifive.bururung.global.common.RedisUtil;
import com.hifive.bururung.global.exception.CustomException;
import com.hifive.bururung.global.exception.errorcode.MemberErrorCode;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
	
	private final JavaMailSender javaMailSender;
	private final RedisUtil redisUtil;
	
    @Value("${spring.mail.username}")
    private String id;
    
	private static final String EMAIL_AUTH_PREFIX = "EMAIL_AUTH:";
	private static final int EMAIL_AUTH_LIMIT = 300;
	
    public String createCode(String email) {
		String code = createCode();
		
		redisUtil.setData(EMAIL_AUTH_PREFIX + email, code, Duration.ofSeconds(EMAIL_AUTH_LIMIT));
		
		return code;
    }
    
    @Async("emailExecutor")
	public void sendAuthenticationMail(EmailType emailType, String email, String code) {		
		try {
	        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
	        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");
	        mimeMessageHelper.setTo(email);
	        mimeMessageHelper.setFrom(new InternetAddress(id+"@naver.com","bururung"));
	        
	        if(emailType == EmailType.SIGNUP) {
	        	mimeMessageHelper.setSubject("[Bururung] 회원가입 인증 코드");        	
	        } else if(emailType == EmailType.CHANGE_PASSWORD) {
	        	mimeMessageHelper.setSubject("[Bururung] 비밀번호 변경 인증 코드");
	        }
	        
	        mimeMessageHelper.setText(createhtml(code), true);
	        
	        javaMailSender.send(mimeMessage);
		} catch(Exception e) {
			throw new CustomException(MemberErrorCode.MAIL_SEND_FAIL);
		}
	}
	
    public boolean isAuthenticated(String email, String code) {
    	String data = redisUtil.getData(EMAIL_AUTH_PREFIX + email);
    	if(data.equals(code)) {
    		return true;
    	}
        
		throw new CustomException(MemberErrorCode.EMAIL_AUTH_FAIL);
    }

	private String createCode() {
        Random r = new Random();
        StringBuilder key = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            int index = r.nextInt(4);

            switch (index) {
                case 0:
                    key.append((char) ((int) r.nextInt(26) + 97));
                    break;
                case 1:
                    key.append((char) ((int) r.nextInt(26) + 65));
                    break;
                default:
                    key.append(r.nextInt(9));
            }
        }
        return key.toString();
    }
	
	private String createhtml(String code) {
		 String html = """
			        <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 10px;">
			            <h2 style="color: #4F46E5; text-align: center;">Bururung 이메일 인증</h2>
			            <p>아래의 <strong>인증 코드 6자리</strong>를 입력하여 이메일 인증을 완료해주세요.</p>
			            
			            <div style="background-color: #F3F4F6; padding: 20px; text-align: center; border-radius: 5px; margin: 20px 0;">
			                <span style="font-size: 24px; font-weight: bold; color: #1D4ED8;">%s</span>
			            </div>
			            
			            <p>⚠️ 이 코드는 <strong>5분</strong>간 유효합니다.</p>
			            <p>만료되기 전에 입력해 주세요.</p>
			            
			            <hr style="margin: 20px 0; border: none; border-top: 1px solid #e0e0e0;">
			            <p style="font-size: 12px; color: #6B7280;">
			                본 메일은 발신 전용입니다. 문의 사항이 있으시면 <a href="mailto:support@bururung.com" style="color: #3B82F6;">support@bururung.com</a>으로 연락해 주세요.
			            </p>
			        </div>
			    """;
		return html.format(html, code);
	}
}