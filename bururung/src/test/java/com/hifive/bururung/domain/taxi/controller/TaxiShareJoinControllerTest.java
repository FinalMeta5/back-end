package com.hifive.bururung.domain.taxi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.hifive.bururung.domain.member.entity.Member;
import com.hifive.bururung.domain.member.service.IMemberService;
import com.hifive.bururung.domain.notification.entity.Notification;
import com.hifive.bururung.domain.notification.service.INotificationService;
import com.hifive.bururung.domain.taxi.dto.TaxiShareJoinRequest;
import com.hifive.bururung.domain.taxi.dto.TaxiShareResponse;
import com.hifive.bururung.domain.taxi.service.ITaxiShareJoinService;
import com.hifive.bururung.domain.taxi.service.ITaxiShareService;
import com.hifive.bururung.domain.taxi.util.TaxiShareJoinAction;
import com.hifive.bururung.global.exception.CustomException;
import com.hifive.bururung.global.exception.errorcode.TaxiShareJoinErrorCode;

@ExtendWith(MockitoExtension.class)
public class TaxiShareJoinControllerTest {

    @InjectMocks
    private TaxiShareJoinController taxiShareJoinController;
    
    @Mock
    private ITaxiShareJoinService taxiShareJoinService;
    
    @Mock
    private ITaxiShareService taxiShareService;
    
    @Mock
    private INotificationService notificationService;
    
    @Mock
    private IMemberService memberService;
    
    // 1. GET /api/taxi/join/count/{taxiShareId}
    @Test
    void testGetJoinCountByTaxiShareId() {
        Long taxiShareId = 100L;
        int joinCount = 5;
        when(taxiShareJoinService.getJoinCountByTaxiShareId(taxiShareId)).thenReturn(joinCount);
        
        ResponseEntity<Integer> response = taxiShareJoinController.getJoinCountByTaxiShareId(taxiShareId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(joinCount, response.getBody());
    }
    
    // 2. POST /api/taxi/join/insert - 성공 케이스: duplCnt < 1 && isHost < 1
    @Test
    void testInsertTaxiShareJoin_Success() {
        // 모의 TaxiShareJoinRequest 생성 (@Mock 사용)
        TaxiShareJoinRequest joinRequest = mock(TaxiShareJoinRequest.class);
        // stubbing: getDuplCnt, getMemberId, getTaxiShareId
        when(taxiShareJoinService.getDuplCntByTaxiShareIdAndMemberId(joinRequest)).thenReturn(0);
        when(taxiShareService.getCountTaxsiShareByIdAndMemberId(joinRequest)).thenReturn(0);
        
        // stubbing: 택시 공유 정보
        TaxiShareResponse taxiShareResponseMock = mock(TaxiShareResponse.class);
        when(joinRequest.getTaxiShareId()).thenReturn(200L);
        when(taxiShareService.getTaxiShareById(200L)).thenReturn(taxiShareResponseMock);
        
        // stubbing: hostInfo와 participantInfo (둘 다 같은 memberId로 처리)
        Long memberId = 10L;
        when(joinRequest.getMemberId()).thenReturn(memberId);
        Member hostInfo = mock(Member.class);
        Member participantInfo = mock(Member.class);
        when(memberService.findByMemberId(memberId)).thenReturn(hostInfo, participantInfo);
        
        // stubbing: static 메서드 TaxiShareJoinAction.getTaxiShareJoinNotiInfo()를 목으로 처리
        Notification notificationMock = mock(Notification.class);
        try (MockedStatic<TaxiShareJoinAction> staticMock = mockStatic(TaxiShareJoinAction.class)) {
            staticMock.when(() -> TaxiShareJoinAction.getTaxiShareJoinNotiInfo(taxiShareResponseMock, joinRequest, participantInfo, hostInfo, 1))
                      .thenReturn(notificationMock);
            staticMock.when(() -> TaxiShareJoinAction.getTaxiShareJoinNotiInfo(taxiShareResponseMock, joinRequest, participantInfo, hostInfo, 2))
                      .thenReturn(notificationMock);
            
            // stubbing: insertCreditByTaxi()와 insertTaxiShareJoin()는 void
            doNothing().when(taxiShareJoinService).insertCreditByTaxi(2, memberId);
            doNothing().when(taxiShareJoinService).insertTaxiShareJoin(joinRequest);
            doNothing().when(notificationService).sendNotification(notificationMock);
            
            ResponseEntity<Void> response = taxiShareJoinController.insertTaxiShareJoin(joinRequest);
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            
            // Verify: 두 번 알림 전송, 크레딧 차감, 참여 insert 호출
            verify(taxiShareJoinService).insertCreditByTaxi(2, memberId);
            verify(notificationService, times(2)).sendNotification(notificationMock);
            verify(taxiShareJoinService).insertTaxiShareJoin(joinRequest);
        }
    }
    
    // 3. POST /api/taxi/join/insert - 실패: duplicate join attempt (duplCnt >= 1)
    @Test
    void testInsertTaxiShareJoin_DuplicateJoin() {
        TaxiShareJoinRequest joinRequest = mock(TaxiShareJoinRequest.class);
        when(taxiShareJoinService.getDuplCntByTaxiShareIdAndMemberId(joinRequest)).thenReturn(1);
        
        CustomException exception = assertThrows(CustomException.class, () -> 
            taxiShareJoinController.insertTaxiShareJoin(joinRequest)
        );
        assertEquals(TaxiShareJoinErrorCode.DUPLICATE_JOIN_ATTEMPT, exception.getErrorCode());
    }
    
    // 4. POST /api/taxi/join/insert - 실패: host joining own share (isHost >= 1)
    @Test
    void testInsertTaxiShareJoin_HostJoinNotAllowed() {
        TaxiShareJoinRequest joinRequest = mock(TaxiShareJoinRequest.class);
        // 중복 참여 없음
        when(taxiShareJoinService.getDuplCntByTaxiShareIdAndMemberId(joinRequest)).thenReturn(0);
        // 호스트인 경우: isHost >= 1
        when(taxiShareService.getCountTaxsiShareByIdAndMemberId(joinRequest)).thenReturn(1);
        
        CustomException exception = assertThrows(CustomException.class, () ->
            taxiShareJoinController.insertTaxiShareJoin(joinRequest)
        );
        assertEquals(TaxiShareJoinErrorCode.CANNOT_JOIN_OWN_SHARE, exception.getErrorCode());
    }
}
