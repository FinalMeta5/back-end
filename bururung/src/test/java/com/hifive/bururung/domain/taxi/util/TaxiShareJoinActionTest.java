package com.hifive.bururung.domain.taxi.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.sql.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hifive.bururung.domain.member.entity.Member;
import com.hifive.bururung.domain.notification.entity.Notification;
import com.hifive.bururung.domain.taxi.dto.TaxiShareJoinRequest;
import com.hifive.bururung.domain.taxi.dto.TaxiShareResponse;

@ExtendWith(MockitoExtension.class)
public class TaxiShareJoinActionTest {

    @Mock
    private TaxiShareResponse taxiShareResponse;
    
    @Mock
    private TaxiShareJoinRequest taxiShareJoinRequest;
    
    @Mock
    private Member participantInfo;
    
    @Mock
    private Member hostInfo;
    
    @Mock
    private Date date = new Date(10);
    
    // 테스트: 참여 알림 (type == 1)
    @Test
    void testGetTaxiShareJoinNotiInfo_Type1() {
        // Stubbing for hostInfo
        when(hostInfo.getNickname()).thenReturn("HostNick");
        
        // Stubbing for taxiShareResponse
        when(taxiShareResponse.getPickupTime()).thenReturn(date);
        when(taxiShareResponse.getPickupTimeOnly()).thenReturn("AM");
        when(taxiShareResponse.getPickupLocation()).thenReturn("LocationA");
        when(taxiShareResponse.getDestination()).thenReturn("DestinationB");
        when(taxiShareResponse.getOpenchatLink()).thenReturn("http://openchat.link");
        when(taxiShareResponse.getOpenchatCode()).thenReturn("OC123");
        
        // Stubbing for taxiShareJoinRequest
        when(taxiShareJoinRequest.getMemberId()).thenReturn(123L);
        
        // Call static method with type 1
        Notification notification = TaxiShareJoinAction.getTaxiShareJoinNotiInfo(
            taxiShareResponse, taxiShareJoinRequest, participantInfo, hostInfo, 1);
        
        assertNotNull(notification);
        // 기본 설정 검증
        assertEquals("택시 공유 서비스", notification.getServiceCtg());
        assertEquals(42L, notification.getSenderId());
        assertNotNull(notification.getCreatedDate());
        
        // 참여 알림 내용: type 1 → 수신자: taxiShareJoinRequest.getMemberId()
        String expectedContent = "호스트 HostNick님의 택시 공유에 참여하셨습니다! 출발시간: 10:00 AM, 출발지: LocationA, 도착지: DestinationB" +
            "\n오픈카톡방에 접속하셔서 의사소통해보세요!" +
            "\n오픈카톡 url: http://openchat.link, 오픈카톡코드: OC123";
        assertEquals(expectedContent, notification.getContent());
        assertEquals("신청완료", notification.getCategory());
        assertEquals(123L, notification.getRecipientId());
    }
    
    // 테스트: 참여 알림 (type == 2, 호스트에게 보낼 알림)
    @Test
    void testGetTaxiShareJoinNotiInfo_Type2() {
        // Stubbing for hostInfo and participantInfo
        when(hostInfo.getNickname()).thenReturn("HostNick");
        when(participantInfo.getNickname()).thenReturn("PartiNick");
        // taxiShareResponse.getMemberId() will be used as recipient for host notification
        when(taxiShareResponse.getMemberId()).thenReturn(555L);
        
        // Call static method with type 2
        Notification notification = TaxiShareJoinAction.getTaxiShareJoinNotiInfo(
            taxiShareResponse, taxiShareJoinRequest, participantInfo, hostInfo, 2);
        
        assertNotNull(notification);
        assertEquals("택시 공유 서비스", notification.getServiceCtg());
        assertEquals(42L, notification.getSenderId());
        assertNotNull(notification.getCreatedDate());
        
        String expectedContent = "HostNick님이 개설하신 택시 공유에 참가자가 생겼습니다! 참가자닉네임: PartiNick";
        assertEquals(expectedContent, notification.getContent());
        assertEquals("신청알림", notification.getCategory());
        assertEquals(555L, notification.getRecipientId());
    }
    
    // 테스트: 삭제 알림
    @Test
    void testGetTaxiShareDeleteNotiInfo() {
        // Stubbing for taxiShareResponse
        when(taxiShareResponse.getPickupLocation()).thenReturn("LocationA");
        when(taxiShareResponse.getDestination()).thenReturn("DestinationB");
        when(taxiShareResponse.getPickupTime()).thenReturn(date);
        when(taxiShareResponse.getPickupTimeOnly()).thenReturn("AM");
        // taxiShareJoinRequest.getTaxiShareId()는 사용되지 in content, so stubbing is optional
        
        // Stubbing for participantInfo
        when(participantInfo.getNickname()).thenReturn("PartiNick");
        when(participantInfo.getMemberId()).thenReturn(888L);
        
        // Call static method for delete notification
        Notification notification = TaxiShareJoinAction.getTaxiShareDeleteNotiInfo(
            taxiShareResponse, taxiShareJoinRequest, participantInfo);
        
        assertNotNull(notification);
        assertEquals("택시 공유 서비스", notification.getServiceCtg());
        assertEquals(42L, notification.getSenderId());
        assertNotNull(notification.getCreatedDate());
        
        String expectedContent = "PartiNick고객님, 죄송합니다. 참여 신청하신 택시 공유가 호스트에 의해 삭제되었습니다. 다른 택시 공유를 신청해보세요!! \n" +
            "삭제된 신청정보\n" +
            " 출발지: LocationA, 도착지: DestinationB, 출발시간: 10:00 AM";
        assertEquals(expectedContent, notification.getContent());
        assertEquals("삭제알림", notification.getCategory());
        assertEquals(888L, notification.getRecipientId());
    }
    
    // 크레딧 차감 메서드는 로직이 구현되어 있지 않으므로, 테스트 대상이 아닙니다.
}
