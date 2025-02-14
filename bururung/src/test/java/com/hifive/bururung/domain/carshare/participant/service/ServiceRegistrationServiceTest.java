package com.hifive.bururung.domain.carshare.participant.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hifive.bururung.domain.carshare.participant.dto.AllCarListResponse;
import com.hifive.bururung.domain.carshare.participant.dto.AvailableCarShareListResponse;
import com.hifive.bururung.domain.carshare.participant.dto.CarInformationResponse;
import com.hifive.bururung.domain.carshare.participant.dto.DriverInformationResponse;
import com.hifive.bururung.domain.carshare.participant.dto.DrivingInformationResponse;
import com.hifive.bururung.domain.carshare.participant.dto.PastParticipationListResponse;
import com.hifive.bururung.domain.carshare.participant.repository.ServiceRegistrationRepository;

@ExtendWith(MockitoExtension.class)
public class ServiceRegistrationServiceTest {

    @InjectMocks
    private ServiceRegistrationService service;

    @Mock
    private ServiceRegistrationRepository repository;
    
    @Mock
    private AvailableCarShareListResponse availableCarShareListResponse;
    
    @Mock
    private DriverInformationResponse driverInformationResponse;
    
    @Mock
    private CarInformationResponse carInformationResponse;
    
    @Mock
    private DrivingInformationResponse drivingInformationResponse;
    
    @Mock
    private AllCarListResponse allCarListResponse;
    
    @Mock
    private PastParticipationListResponse pastParticipationListResponse;

    // 1. 현재 이용 가능한 공유 차량 목록
    @Test
    void testGetAvailableCarShareList() {
        List<AvailableCarShareListResponse> list = new ArrayList<>();
        list.add(availableCarShareListResponse);
        when(repository.findAvailableCarShareList()).thenReturn(list);

        List<AvailableCarShareListResponse> result = service.getAvailableCarShareList();
        assertEquals(list, result);
        verify(repository).findAvailableCarShareList();
    }

    // 2. 운전자 정보 조회
    @Test
    void testGetDriverInformation() {
        Long memberId = 1L;
        DriverInformationResponse driverInfo = driverInformationResponse;
        when(repository.findDriverInformation(memberId)).thenReturn(driverInfo);

        DriverInformationResponse result = service.getDriverInformation(memberId);
        assertEquals(driverInfo, result);
        verify(repository).findDriverInformation(memberId);
    }

    // 3. 차량 정보 조회
    @Test
    void testGetCarInformation() {
        Long memberId = 1L;
        CarInformationResponse carInfo = carInformationResponse;
        when(repository.findCarInformation(memberId)).thenReturn(carInfo);

        CarInformationResponse result = service.getCarInformation(memberId);
        assertEquals(carInfo, result);
        verify(repository).findCarInformation(memberId);
    }

    // 4. 차량 운행 정보 조회
    @Test
    void testGetDrivingInformation() {
        Long memberId = 1L;
        Long carShareRegiId = 10L;
        DrivingInformationResponse drivingInfo = drivingInformationResponse;
        // repository.findDrivingInformation()는 파라미터로 Map을 받음
        when(repository.findDrivingInformation(any(Map.class))).thenReturn(drivingInfo);

        DrivingInformationResponse result = service.getDrivingInformation(memberId, carShareRegiId);
        assertEquals(drivingInfo, result);
        // 전달된 Map이 올바른 값을 포함하는지 검증
        verify(repository).findDrivingInformation(argThat(map ->
            map instanceof HashMap &&
            map.get("memberId").equals(memberId) &&
            map.get("carShareRegiId").equals(carShareRegiId)
        ));
    }

    // 5. 공유 차량 예약
    // (1) 잔여 크레딧 부족: leftoverCredit < 7 → false 반환 및 insertRegistration 미호출
    @Test
    void testInsertRegistration_InsufficientCredit() {
        Long carShareRegiId = 10L;
        Long userId = 1L;
        when(repository.findLeftoverCredit(userId)).thenReturn((int) 5L);  // 5 < 7

        boolean result = service.insertRegistration(carShareRegiId, userId);
        assertFalse(result);
        verify(repository).findLeftoverCredit(userId);
        verify(repository, never()).insertRegistration(any(Map.class));
    }

    // (2) 잔여 크레딧 충분: leftoverCredit >= 7 → true 반환 및 insertRegistration 호출
    @Test
    void testInsertRegistration_SufficientCredit() {
        Long carShareRegiId = 10L;
        Long userId = 1L;
        when(repository.findLeftoverCredit(userId)).thenReturn((int) 7L); // 7 >= 7

        boolean result = service.insertRegistration(carShareRegiId, userId);
        assertTrue(result);
        verify(repository).findLeftoverCredit(userId);
        verify(repository).insertRegistration(argThat(map -> 
            map instanceof HashMap &&
            map.get("carShareRegiId").equals(carShareRegiId) &&
            map.get("userId").equals(userId)
        ));
    }

    // 6. 리뷰 평점 조회
    @Test
    void testFindRating() {
        Long memberId = 1L;
        Double rating = 4.5;
        when(repository.findRating(memberId)).thenReturn(rating);

        Double result = service.findRating(memberId);
        assertEquals(rating, result);
        verify(repository).findRating(memberId);
    }

    // 7. 잔여 크레딧 조회
    @Test
    void testFindLeftoverCredit() {
        Long userId = 1L;
        int leftover = 10;
        when(repository.findLeftoverCredit(userId)).thenReturn(leftover);

        int result = service.findLeftoverCredit(userId);
        assertEquals(leftover, result);
        verify(repository).findLeftoverCredit(userId);
    }

    // 8. 크레딧 차감
    @Test
    void testInsertCreditByCar() {
        Long userId = 1L;
        // 단순히 repository.insertCreditByCar 호출 여부 검증
        service.insertCreditByCar(userId);
        verify(repository).insertCreditByCar(userId);
    }

    // 9. 전체 공유 차량 목록 조회
    @Test
    void testFindAllShareCarList() {
        List<AllCarListResponse> list = new ArrayList<>();
        list.add(allCarListResponse);
        when(repository.findAllShareCarList()).thenReturn(list);

        List<AllCarListResponse> result = service.findAllShareCarList();
        assertEquals(list, result);
        verify(repository).findAllShareCarList();
    }

    // 10. 과거 차량 탑승 내역 조회
    @Test
    void testFindPastParticipationList() {
        Long userId = 1L;
        PastParticipationListResponse pastResponse = pastParticipationListResponse;
        when(repository.findPastParticipationList(userId)).thenReturn(pastResponse);

        PastParticipationListResponse result = service.findPastParticipationList(userId);
        assertEquals(pastResponse, result);
        verify(repository).findPastParticipationList(userId);
    }

    // 11. 오늘 차량 탑승 내역 조회
    // (1) repository가 null을 반환하면 빈 리스트 반환
    @Test
    void testFindTodayParticipationList_Null() {
        Long userId = 1L;
        when(repository.findTodayParticipationList(userId)).thenReturn(null);

        List<PastParticipationListResponse> result = service.findTodayParticipationList(userId);
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository).findTodayParticipationList(userId);
    }

    // (2) repository가 non-null 리스트 반환
    @Test
    void testFindTodayParticipationList_NonNull() {
        Long userId = 1L;
        List<PastParticipationListResponse> list = new ArrayList<>();
        list.add(pastParticipationListResponse);
        when(repository.findTodayParticipationList(userId)).thenReturn(list);

        List<PastParticipationListResponse> result = service.findTodayParticipationList(userId);
        assertEquals(list, result);
        verify(repository).findTodayParticipationList(userId);
    }
}
