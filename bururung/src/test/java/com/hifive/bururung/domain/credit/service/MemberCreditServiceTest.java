package com.hifive.bururung.domain.credit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hifive.bururung.domain.credit.entity.MemberCredit;
import com.hifive.bururung.domain.credit.entity.MemberCreditState;
import com.hifive.bururung.domain.credit.repository.MemberCreditRepository;
import com.hifive.bururung.domain.member.entity.Member;
import com.hifive.bururung.domain.member.repository.MemberRepository;
import com.hifive.bururung.global.exception.CustomException;
import com.hifive.bururung.global.exception.errorcode.MemberErrorCode;

@ExtendWith(MockitoExtension.class)
public class MemberCreditServiceTest {

    @InjectMocks
    private MemberCreditService memberCreditService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberCreditRepository memberCreditRepository;
    
    @Mock
    private Member member;

    // 정상 분기: 회원이 존재할 때, credit 충전 후 MemberCredit이 저장되는지 검증
    @Test
    void testChargeCredit_Success() {
        Long memberId = 1L;
        int creditCount = 50;
        // member의 초기 상태는 테스트 환경에 맞게 설정(예: credit 값)
        
        // memberRepository.findById()가 Optional.of(member)를 반환하도록 설정
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        
        // 서비스 메서드 호출
        memberCreditService.chargeCredit(memberId, creditCount);
        
        // memberCreditRepository.save()에 전달된 MemberCredit 객체를 캡처하여 검증
        ArgumentCaptor<MemberCredit> captor = ArgumentCaptor.forClass(MemberCredit.class);
        verify(memberCreditRepository).save(captor.capture());
        MemberCredit savedMemberCredit = captor.getValue();
        
        assertNotNull(savedMemberCredit);
        assertEquals(member, savedMemberCredit.getMember());
        assertEquals(creditCount, savedMemberCredit.getCount());
        assertEquals(MemberCreditState.CHARGE, savedMemberCredit.getState());
    }
    
    // 예외 분기: 회원이 존재하지 않으면 CustomException(USER_NOT_FOUND)이 발생하는지 검증
    @Test
    void testChargeCredit_MemberNotFound() {
        Long memberId = 1L;
        int creditCount = 50;
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());
        
        CustomException exception = assertThrows(CustomException.class, () -> {
            memberCreditService.chargeCredit(memberId, creditCount);
        });
        assertEquals(MemberErrorCode.USER_NOT_FOUND, exception.getErrorCode());
        
        verify(memberRepository).findById(memberId);
        verify(memberCreditRepository, never()).save(any(MemberCredit.class));
    }
}

