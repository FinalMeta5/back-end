package com.hifive.bururung.domain.member.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.util.Collection;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.hifive.bururung.domain.member.entity.Member;
import com.hifive.bururung.domain.member.repository.MemberRepository;
import com.hifive.bururung.global.exception.CustomException;
import com.hifive.bururung.global.exception.errorcode.MemberErrorCode;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsServiceImpl;
    
    @Mock
    private MemberRepository memberRepository;
    
    // Member 도메인 객체는 new 키워드 대신 @Spy로 생성
    @Spy
    private Member memberSpy = mock(Member.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
    
    @BeforeEach
    void setUp() {
        // 필요한 스터빙은 각 테스트에서 진행합니다.
    }
    
    @Test
    void testLoadUserByUsername_Success() {
        String email = "user@example.com";
        // stubbing: Member 객체의 getter 결과 설정
        when(memberSpy.getMemberId()).thenReturn(1L);
        when(memberSpy.getPassword()).thenReturn("encodedPassword");
        when(memberSpy.getRoleName()).thenReturn("ROLE_USER");
        
        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(memberSpy));
        
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(email);
        
        // UserDetails의 username는 memberId의 문자열 값 ("1")
        assertEquals("1", userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        
        // 권한 목록 검증
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertNotNull(authorities);
        assertTrue(authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
    }
    
    @Test
    void testLoadUserByUsername_NotFound() {
        String email = "nonexistent@example.com";
        when(memberRepository.findByEmail(email)).thenReturn(Optional.empty());
        
        CustomException exception = assertThrows(CustomException.class, () -> 
            userDetailsServiceImpl.loadUserByUsername(email)
        );
        assertEquals(MemberErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }
}
