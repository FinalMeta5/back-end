package com.hifive.bururung.domain.taxi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

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
import com.hifive.bururung.domain.taxi.dto.LatLng;
import com.hifive.bururung.domain.taxi.dto.TaxiShareJoinRequest;
import com.hifive.bururung.domain.taxi.dto.TaxiShareResponse;
import com.hifive.bururung.domain.taxi.entity.TaxiShare;
import com.hifive.bururung.domain.taxi.service.ITaxiShareJoinService;
import com.hifive.bururung.domain.taxi.service.ITaxiShareService;
import com.hifive.bururung.domain.taxi.util.TaxiShareJoinAction;
import com.hifive.bururung.global.common.NearbyLocationFinder;

@ExtendWith(MockitoExtension.class)
public class TaxiShareControllerTest {

    @InjectMocks
    private TaxiShareController taxiShareController;
    
    @Mock
    private ITaxiShareService taxiShareService;
    
    @Mock
    private ITaxiShareJoinService taxiShareJoinService;
    
    @Mock
    private IMemberService memberService;
    
    @Mock
    private INotificationService notificationService;
    
    // 1. POST /api/taxi/insert
    @Test
    void testInsertTaxiShare() {
        // TaxiShare 도메인 객체는 @Mock으로 생성
        TaxiShare taxiShareMock = mock(TaxiShare.class);
        
        // insertTaxiShare() 호출
        ResponseEntity<Void> response = taxiShareController.insertTaxiShare(taxiShareMock);
        
        // taxiShareService.insertTaxiShare() 호출 여부 검증
        verify(taxiShareService).insertTaxiShare(taxiShareMock);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
    
    // 2. POST /api/taxi/list - target == null (날짜로만 검색)
    @Test
    void testGetTaxiShareByPickupTimeAndLatLng_TargetNull() {
        String pickupTime = "2025-02-14T06:00";
        List<TaxiShareResponse> taxiShareResponseList = mock(List.class);
        when(taxiShareService.getTaxiShareByPickupTime(pickupTime)).thenReturn(taxiShareResponseList);
        
        ResponseEntity<List<TaxiShareResponse>> response = taxiShareController.getTaxiShareByPickupTimeAndLatLng(pickupTime, null);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(taxiShareResponseList, response.getBody());
    }
    
    // 3. POST /api/taxi/list - target != null (날짜+위치 검색)
    @Test
    void testGetTaxiShareByPickupTimeAndLatLng_WithTarget() {
        String pickupTime = "2025-02-14T06:00";
        // 모의 TaxiShareResponse 목록 생성
        TaxiShareResponse tsResponse1 = mock(TaxiShareResponse.class);
        TaxiShareResponse tsResponse2 = mock(TaxiShareResponse.class);
        when(tsResponse1.getLatitudePL()).thenReturn(10.0);
        when(tsResponse1.getLongitudePL()).thenReturn(20.0);
        when(tsResponse2.getLatitudePL()).thenReturn(30.0);
        when(tsResponse2.getLongitudePL()).thenReturn(40.0);
        List<TaxiShareResponse> fullList = List.of(tsResponse1, tsResponse2);
        when(taxiShareService.getTaxiShareByPickupTime(pickupTime)).thenReturn(fullList);
        
        // 타겟 위치 (목 객체)
        LatLng target = mock(LatLng.class);
        when(target.getLat()).thenReturn(10.0);
        when(target.getLng()).thenReturn(20.0);
        
        // allLocations: 리스트 생성 로직은 컨트롤러 내부에서 new ArrayList<>() 사용됨
        // nearbyLocations: 모의 NearbyLocationFinder.findNearbyLocations()를 통해 제어
        LatLng nearbyLatLng = mock(LatLng.class);
        when(nearbyLatLng.getLat()).thenReturn(10.0);
        when(nearbyLatLng.getLng()).thenReturn(20.0);
        List<LatLng> nearbyLocations = List.of(nearbyLatLng);
        
        // Mock static 메서드 NearbyLocationFinder.findNearbyLocations()
        try (MockedStatic<NearbyLocationFinder> nearbyFinderMock = mockStatic(NearbyLocationFinder.class)) {
            nearbyFinderMock.when(() -> NearbyLocationFinder.findNearbyLocations(eq(target), any()))
                             .thenReturn(nearbyLocations);
            
            ResponseEntity<List<TaxiShareResponse>> response = taxiShareController.getTaxiShareByPickupTimeAndLatLng(pickupTime, target);
            
            // tsResponse1의 좌표와 nearbyLocations 일치하므로 결과에 포함되어야 함.
            List<TaxiShareResponse> resultList = response.getBody();
            assertNotNull(resultList);
            assertEquals(1, resultList.size());
            assertEquals(tsResponse1, resultList.get(0));
        }
    }
    
    // 4. GET /api/taxi/detail/{taxiShareId}
    @Test
    void testGetTaxiShareById() {
        Long taxiShareId = 100L;
        TaxiShareResponse taxiShareResponseMock = mock(TaxiShareResponse.class);
        when(taxiShareService.getTaxiShareById(taxiShareId)).thenReturn(taxiShareResponseMock);
        
        ResponseEntity<TaxiShareResponse> response = taxiShareController.getTaxiShareById(taxiShareId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(taxiShareResponseMock, response.getBody());
    }
    
    // 5. POST /api/taxi/delete
    @Test
    void testDeleteTaxiShare() {
        // 모의 TaxiShareJoinRequest 생성
        TaxiShareJoinRequest joinRequest = mock(TaxiShareJoinRequest.class);
        // 택시 공유 ID 및 삭제 요청 회원 ID stubbing
        Long taxiShareId = 200L;
        Long requesterMemberId = 10L;  // 삭제 요청한 사람
        when(joinRequest.getTaxiShareId()).thenReturn(taxiShareId);
        when(joinRequest.getMemberId()).thenReturn(requesterMemberId);
        
        // 모의 TaxiShareResponse 생성 (삭제되는 게시글 작성자 ID)
        TaxiShareResponse taxiShareResponseMock = mock(TaxiShareResponse.class);
        Long actualOwnerId = 5L;
        when(taxiShareResponseMock.getMemberId()).thenReturn(actualOwnerId);
        when(taxiShareService.getTaxiShareById(taxiShareId)).thenReturn(taxiShareResponseMock);
        
        // 택시 공유에 신청한 멤버 목록 stubbing (예: 2명의 참가자)
        List<Long> participantMemberIds = List.of(20L, 30L);
        when(taxiShareJoinService.getMemberIdByTaxiShareId(taxiShareId)).thenReturn(participantMemberIds);
        
        // 각 참가자의 Member 객체를 모의(@Mock)로 생성
        Member participant1 = mock(Member.class);
        Member participant2 = mock(Member.class);
        when(memberService.findByMemberId(20L)).thenReturn(participant1);
        when(memberService.findByMemberId(30L)).thenReturn(participant2);
        
        // static 메서드 TaxiShareJoinAction.getTaxiShareDeleteNotiInfo()를 모의
        Notification notificationMock = mock(Notification.class);
        try (MockedStatic<TaxiShareJoinAction> taxiShareJoinActionMock = mockStatic(TaxiShareJoinAction.class)) {
            taxiShareJoinActionMock.when(() ->
                TaxiShareJoinAction.getTaxiShareDeleteNotiInfo(taxiShareResponseMock, joinRequest, participant1)
            ).thenReturn(notificationMock);
            taxiShareJoinActionMock.when(() ->
                TaxiShareJoinAction.getTaxiShareDeleteNotiInfo(taxiShareResponseMock, joinRequest, participant2)
            ).thenReturn(notificationMock);
            
            // deleteTaxiShare() 호출
            ResponseEntity<Void> response = taxiShareController.deleteTaxiShare(joinRequest);
            
            // Verify: notificationService.sendNotification()가 각 참가자에 대해 호출
            verify(notificationService, times(2)).sendNotification(notificationMock);
            // Verify: taxiShareService.deleteTaxiShare(joinRequest) 호출
            verify(taxiShareService).deleteTaxiShare(joinRequest);
            
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }
}
