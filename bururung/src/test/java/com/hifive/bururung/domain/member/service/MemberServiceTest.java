package com.hifive.bururung.domain.member.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import com.hifive.bururung.domain.member.dto.LoginResponse;
import com.hifive.bururung.domain.member.dto.SignupRequest;
import com.hifive.bururung.domain.member.dto.TokenDTO;
import com.hifive.bururung.domain.member.entity.Member;
import com.hifive.bururung.domain.member.repository.MemberRepository;
import com.hifive.bururung.global.common.RedisUtil;
import com.hifive.bururung.global.common.TokenProvider;
import com.hifive.bururung.global.exception.CustomException;
import com.hifive.bururung.global.exception.errorcode.MemberErrorCode;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    
    @InjectMocks
    private MemberService memberService;
    
    @Mock
    private MemberRepository memberRepository;
    
    @Mock
    private TokenProvider tokenProvider;
    
    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    
    @Mock
    private RedisUtil redisUtil;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    // refreshTokenValidity를 주입 (예: 3600초)
    private final Long refreshTokenValidity = 3600L;
    
    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(memberService, "refreshTokenValidity", refreshTokenValidity);
    }
    
    // -------------------------------------
    // 1. login()
    // -------------------------------------
    @Test
    void testLogin_Success() {
        String email = "user@example.com";
        String password = "password";
        
        // @Mock으로 생성된 Authentication 객체
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(email);
        
        // AuthenticationManager도 @Mock으로 생성
        AuthenticationManager authManager = mock(AuthenticationManager.class);
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(authentication);
        when(authenticationManagerBuilder.getObject()).thenReturn(authManager);
        
        // Stub TokenProvider
        String accessToken = "access-token";
        String refreshToken = "refresh-token";
        when(tokenProvider.createAccessToken(authentication)).thenReturn(accessToken);
        when(tokenProvider.createRefreshToken(authentication)).thenReturn(refreshToken);
        
        // Call login
        TokenDTO tokenDTO = memberService.login(email, password);
        
        // Verify redisUtil.setData() 호출
        String expectedKey = "REFRESH_TOKEN:" + email;
        ArgumentCaptor<Duration> durationCaptor = ArgumentCaptor.forClass(Duration.class);
        verify(redisUtil).setData(eq(expectedKey), eq(refreshToken), durationCaptor.capture());
        assertEquals(Duration.ofSeconds(refreshTokenValidity), durationCaptor.getValue());
        
        // Verify TokenDTO 내용
        assertNotNull(tokenDTO);
        assertEquals(accessToken, tokenDTO.getAccessToken());
        assertEquals(refreshToken, tokenDTO.getRefreshToken());
    }
    
    // -------------------------------------
    // 2. signup()
    // -------------------------------------
    @Test
    void testSignup_Success() {
        // SignupRequest를 @Mock으로 생성하고 stubbing
        SignupRequest signupRequest = mock(SignupRequest.class);
        when(signupRequest.getBirth()).thenReturn("2000-01-01");
        when(signupRequest.getEmail()).thenReturn("newuser@example.com");
        when(signupRequest.getGender()).thenReturn('M');
        when(signupRequest.getName()).thenReturn("New User");
        when(signupRequest.getPassword()).thenReturn("plainPassword");
        when(signupRequest.getPhone()).thenReturn("01012345678");
        when(signupRequest.getNickname()).thenReturn("newbie");
        
        // Simulate password encoding
        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode("plainPassword")).thenReturn(encodedPassword);
        
        // memberRepository.save()가 호출되었을 때, 스파이 객체(Member)를 반환하도록 구성
        // Member는 도메인 객체이므로 @Spy로 생성 (new 키워드 없이 Mockito.spy() 사용)
        Member memberSpy = spy(Member.class);
        doReturn(1L).when(memberSpy).getMemberId();
        // save() 메서드는 인자로 받은 객체를 그대로 반환하도록 stubbing
        when(memberRepository.save(any(Member.class))).thenReturn(memberSpy);
        
        Long memberId = memberService.signup(signupRequest);
        assertEquals(1L, memberId);
        
        // 캡처해서 전달된 Member 객체의 stubbing된 getter값 확인
        ArgumentCaptor<Member> memberCaptor = ArgumentCaptor.forClass(Member.class);
        verify(memberRepository).save(memberCaptor.capture());
        Member capturedMember = memberCaptor.getValue();
        // stubbing된 값는 SignupRequest의 getter 결과로 예상됨
        assertEquals("2000-01-01", capturedMember.getBirth());
        assertEquals("newuser@example.com", capturedMember.getEmail());
        assertEquals("M", capturedMember.getGender());
        assertEquals("New User", capturedMember.getName());
        assertEquals("ROLE_USER", capturedMember.getRoleName());
        assertEquals(encodedPassword, capturedMember.getPassword());
        assertEquals("01012345678", capturedMember.getPhone());
        assertEquals("newbie", capturedMember.getNickname());
    }
    
    // -------------------------------------
    // 3. findEmail()
    // -------------------------------------
    @Test
    void testFindEmail_Success() {
        String name = "User";
        String phone = "01012345678";
        String foundEmail = "user@example.com";
        
        when(memberRepository.findEmailByNameAndPhone(name, phone)).thenReturn(Optional.of(foundEmail));
        
        String result = memberService.findEmail(name, phone);
        assertEquals(foundEmail, result);
    }
    
    @Test
    void testFindEmail_NotFound() {
        String name = "User";
        String phone = "01012345678";
        
        when(memberRepository.findEmailByNameAndPhone(name, phone)).thenReturn(Optional.empty());
        
        CustomException exception = assertThrows(CustomException.class, () -> {
            memberService.findEmail(name, phone);
        });
        assertEquals(MemberErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }
    
    // -------------------------------------
    // 4. checkMemberExists()
    // -------------------------------------
    @Test
    void testCheckMemberExists_True() {
        Long memberId = 1L;
        when(memberRepository.existsById(memberId)).thenReturn(true);
        
        assertTrue(memberService.checkMemberExists(memberId));
    }
    
    @Test
    void testCheckMemberExists_False() {
        Long memberId = 1L;
        when(memberRepository.existsById(memberId)).thenReturn(false);
        
        assertFalse(memberService.checkMemberExists(memberId));
    }
    
    // -------------------------------------
    // 5. findByMemberId()
    // -------------------------------------
    @Test
    void testFindByMemberId_Success() {
        Long memberId = 1L;
        // Member 객체는 @Spy로 생성 (new 키워드 없이 Mockito.spy() 사용)
        Member memberSpy = spy(Member.class);
        doReturn(memberId).when(memberSpy).getMemberId();
        
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(memberSpy));
        
        Member result = memberService.findByMemberId(memberId);
        assertEquals(memberSpy, result);
    }
    
    @Test
    void testFindByMemberId_NotFound() {
        Long memberId = 1L;
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());
        
        CustomException exception = assertThrows(CustomException.class, () -> {
            memberService.findByMemberId(memberId);
        });
        assertEquals(MemberErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }
    
    // -------------------------------------
    // 6. changePassword()
    // -------------------------------------
    @Test
    void testChangePassword_Success() {
        String email = "user@example.com";
        String newPassword = "newPassword";
        String encodedPassword = "encodedNewPassword";
        
        // Member 객체를 @Spy로 생성
        Member memberSpy = spy(Member.class);
        doReturn(1L).when(memberSpy).getMemberId();
        // 초기 비밀번호는 "oldPassword" (stubbing)
        doReturn("oldPassword").when(memberSpy).getPassword();
        
        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(memberSpy));
        when(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword);
        
        // 만약 changePassword() 메서드가 실제로 멤버의 상태를 변경한다면,
        // 스파이 객체에서 해당 메서드를 호출하도록 하고, 그 후 getPassword()를 stubbing 변경
        doAnswer(invocation -> {
            String pwd = invocation.getArgument(0);
            // stubbing: getPassword() returns encodedPassword
            doReturn(encodedPassword).when(memberSpy).getPassword();
            return null;
        }).when(memberSpy).changePassword(anyString());
        
        Long resultMemberId = memberService.changePassword(email, newPassword);
        assertEquals(1L, resultMemberId);
        // verify that the member's password was updated to encodedPassword via stubbing
        assertEquals(encodedPassword, memberSpy.getPassword());
    }
    
    @Test
    void testChangePassword_NotFound() {
        String email = "nonexistent@example.com";
        String newPassword = "newPassword";
        
        when(memberRepository.findByEmail(email)).thenReturn(Optional.empty());
        
        CustomException exception = assertThrows(CustomException.class, () -> {
            memberService.changePassword(email, newPassword);
        });
        assertEquals(MemberErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }
    
    // -------------------------------------
    // 7. getLoginResponse()
    // -------------------------------------
    @Test
    void testGetLoginResponse_Success() {
        String email = "user@example.com";
        // Member 객체를 @Spy로 생성
        Member memberSpy = spy(Member.class);
        doReturn(1L).when(memberSpy).getMemberId();
        doReturn("nickname").when(memberSpy).getNickname();
        doReturn("ROLE_USER").when(memberSpy).getRoleName();
        
        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(memberSpy));
        
        LoginResponse response = memberService.getLoginResponse(email);
        assertEquals(1L, response.getMemberId());
        assertEquals("nickname", response.getNickname());
        assertEquals("ROLE_USER", response.getRole());
    }
    
    @Test
    void testGetLoginResponse_NotFound() {
        String email = "nonexistent@example.com";
        when(memberRepository.findByEmail(email)).thenReturn(Optional.empty());
        
        CustomException exception = assertThrows(CustomException.class, () -> {
            memberService.getLoginResponse(email);
        });
        assertEquals(MemberErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }
}
