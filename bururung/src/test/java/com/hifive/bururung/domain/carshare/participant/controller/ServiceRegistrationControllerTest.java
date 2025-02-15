package com.hifive.bururung.domain.carshare.participant.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.hifive.bururung.domain.carshare.participant.dto.AllCarListResponse;
import com.hifive.bururung.domain.carshare.participant.dto.AvailableCarShareListResponse;
import com.hifive.bururung.domain.carshare.participant.dto.CarInformationResponse;
import com.hifive.bururung.domain.carshare.participant.dto.DriverInformationResponse;
import com.hifive.bururung.domain.carshare.participant.dto.DrivingInformationResponse;
import com.hifive.bururung.domain.carshare.participant.dto.PastParticipationListResponse;
import com.hifive.bururung.domain.carshare.participant.service.IServiceRegistrationService;

@ExtendWith(MockitoExtension.class)
public class ServiceRegistrationControllerTest {

    @InjectMocks
    private ServiceRegistrationController controller;

    @Mock
    private IServiceRegistrationService registrationService;
    
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

    // 1. GET /available-list
    @Test
    void testGetAvailableCarShareList_Empty() {
        when(registrationService.getAvailableCarShareList()).thenReturn(Collections.emptyList());

        ResponseEntity<List<AvailableCarShareListResponse>> response = controller.getAvailableCarShareList();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(registrationService).getAvailableCarShareList();
    }

    @Test
    void testGetAvailableCarShareList_Success() {
        List<AvailableCarShareListResponse> list = new ArrayList<>();
        list.add(availableCarShareListResponse);
        when(registrationService.getAvailableCarShareList()).thenReturn(list);

        ResponseEntity<List<AvailableCarShareListResponse>> response = controller.getAvailableCarShareList();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(list, response.getBody());
        verify(registrationService).getAvailableCarShareList();
    }

    @Test
    void testGetAvailableCarShareList_Exception() {
        when(registrationService.getAvailableCarShareList()).thenThrow(new RuntimeException("error"));

        ResponseEntity<List<AvailableCarShareListResponse>> response = controller.getAvailableCarShareList();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(registrationService).getAvailableCarShareList();
    }

    // 2. GET /driver-information/{memberId}
    @Test
    void testGetDriverInformation_Success() {
        Long memberId = 1L;
        DriverInformationResponse driverInfo = driverInformationResponse;
        when(registrationService.getDriverInformation(memberId)).thenReturn(driverInfo);

        ResponseEntity<Object> response = controller.getCarShareDetailInformation(memberId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(driverInfo, response.getBody());
        verify(registrationService).getDriverInformation(memberId);
    }

    @Test
    void testGetDriverInformation_NullResponse() {
        Long memberId = 1L;
        when(registrationService.getDriverInformation(memberId)).thenReturn(null);

        ResponseEntity<Object> response = controller.getCarShareDetailInformation(memberId);
        String expectedMessage = "(정보) " + memberId + "번 사용자는 운전자로 등록되어 있지 않습니다.";

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(expectedMessage, response.getBody());
        verify(registrationService).getDriverInformation(memberId);
    }

    @Test
    void testGetDriverInformation_RuntimeException() {
        Long memberId = 1L;
        when(registrationService.getDriverInformation(memberId)).thenThrow(new RuntimeException("dummy"));

        ResponseEntity<Object> response = controller.getCarShareDetailInformation(memberId);
        String expectedMessage = "(정보) " + memberId + "번 사용자는 운전자로 등록되어 있지 않습니다.";

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(expectedMessage, response.getBody());
        verify(registrationService).getDriverInformation(memberId);
    }

    @Test
    void testGetDriverInformation_Exception() {
        Long memberId = 1L;
        when(registrationService.getDriverInformation(memberId)).thenThrow(new Exception("dummy"));

        ResponseEntity<Object> response = controller.getCarShareDetailInformation(memberId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(registrationService).getDriverInformation(memberId);
    }

    // 3. GET /car-information/{memberId}
    @Test
    void testGetCarInformation_Success() {
        Long memberId = 1L;
        CarInformationResponse carInfo = carInformationResponse;
        when(registrationService.getCarInformation(memberId)).thenReturn(carInfo);

        ResponseEntity<Object> response = controller.getCarInformation(memberId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(carInfo, response.getBody());
        verify(registrationService).getCarInformation(memberId);
    }

    @Test
    void testGetCarInformation_NullResponse() {
        Long memberId = 1L;
        when(registrationService.getCarInformation(memberId)).thenReturn(null);

        ResponseEntity<Object> response = controller.getCarInformation(memberId);
        String expectedMessage = "(정보) " + memberId + "번 사용자의 차량은 등록되어 있지 않습니다.";

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(expectedMessage, response.getBody());
        verify(registrationService).getCarInformation(memberId);
    }

    @Test
    void testGetCarInformation_RuntimeException() {
        Long memberId = 1L;
        when(registrationService.getCarInformation(memberId)).thenThrow(new RuntimeException("dummy"));

        ResponseEntity<Object> response = controller.getCarInformation(memberId);
        String expectedMessage = "(정보) " + memberId + "번 사용자의 차량은 등록되어 있지 않습니다.";

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(expectedMessage, response.getBody());
        verify(registrationService).getCarInformation(memberId);
    }

    @Test
    void testGetCarInformation_Exception() {
        Long memberId = 1L;
        when(registrationService.getCarInformation(memberId)).thenThrow(new Exception("dummy"));

        ResponseEntity<Object> response = controller.getCarInformation(memberId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(registrationService).getCarInformation(memberId);
    }

    // 4. GET /driving-information?memberId=&carShareRegiId=
    @Test
    void testGetDrivingInformation_Success() {
        Long memberId = 1L;
        Long carShareRegiId = 10L;
        DrivingInformationResponse drivingInfo = drivingInformationResponse;
        when(registrationService.getDrivingInformation(memberId, carShareRegiId))
            .thenReturn(drivingInfo);

        ResponseEntity<Object> response = controller.getDrivingInformation(memberId, carShareRegiId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(drivingInfo, response.getBody());
        verify(registrationService).getDrivingInformation(memberId, carShareRegiId);
    }

    @Test
    void testGetDrivingInformation_NullResponse() {
        Long memberId = 1L;
        Long carShareRegiId = 10L;
        when(registrationService.getDrivingInformation(memberId, carShareRegiId))
            .thenReturn(null);
        String expectedMessage = "(정보) " + carShareRegiId + "번 차량 공유에 대한 운행 정보가 없습니다.";

        ResponseEntity<Object> response = controller.getDrivingInformation(memberId, carShareRegiId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(expectedMessage, response.getBody());
        verify(registrationService).getDrivingInformation(memberId, carShareRegiId);
    }

    @Test
    void testGetDrivingInformation_RuntimeException() {
        Long memberId = 1L;
        Long carShareRegiId = 10L;
        when(registrationService.getDrivingInformation(memberId, carShareRegiId))
            .thenThrow(new RuntimeException("dummy"));
        String expectedMessage = "(정보) " + carShareRegiId + "번 차량 공유에 대한 운행 정보가 없습니다.";

        ResponseEntity<Object> response = controller.getDrivingInformation(memberId, carShareRegiId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(expectedMessage, response.getBody());
        verify(registrationService).getDrivingInformation(memberId, carShareRegiId);
    }

    @Test
    void testGetDrivingInformation_Exception() {
        Long memberId = 1L;
        Long carShareRegiId = 10L;
        when(registrationService.getDrivingInformation(memberId, carShareRegiId))
            .thenThrow(new Exception("dummy"));

        ResponseEntity<Object> response = controller.getDrivingInformation(memberId, carShareRegiId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(registrationService).getDrivingInformation(memberId, carShareRegiId);
    }

    // 5. POST /reservation
    @Test
    void testRegisterCarShare_InsufficientCredit() {
        Long carShareRegiId = 10L;
        Long userId = 1L;
        // leftCredit < 7
        when(registrationService.findLeftoverCredit(userId)).thenReturn(5);

        String result = controller.registerCarShare(carShareRegiId, userId);
        assertEquals("잔여 크레딧이 부족합니다.", result);

        verify(registrationService).findLeftoverCredit(userId);
        verify(registrationService, never()).insertRegistration(anyLong(), anyLong());
    }

    @Test
    void testRegisterCarShare_SuccessReservation() {
        Long carShareRegiId = 10L;
        Long userId = 1L;
        when(registrationService.findLeftoverCredit(userId)).thenReturn(10);
        when(registrationService.insertRegistration(carShareRegiId, userId)).thenReturn(true);

        String result = controller.registerCarShare(carShareRegiId, userId);
        assertEquals("차량 공유 예약에 성공했습니다.", result);

        verify(registrationService).findLeftoverCredit(userId);
        verify(registrationService).insertRegistration(carShareRegiId, userId);
    }

    @Test
    void testRegisterCarShare_FailedReservation() {
        Long carShareRegiId = 10L;
        Long userId = 1L;
        when(registrationService.findLeftoverCredit(userId)).thenReturn(10);
        when(registrationService.insertRegistration(carShareRegiId, userId)).thenReturn(false);

        String result = controller.registerCarShare(carShareRegiId, userId);
        assertEquals("차량 공유 예약에 실패했습니다.", result);

        verify(registrationService).findLeftoverCredit(userId);
        verify(registrationService).insertRegistration(carShareRegiId, userId);
    }

    @Test
    void testRegisterCarShare_Exception() {
        Long carShareRegiId = 10L;
        Long userId = 1L;
        when(registrationService.findLeftoverCredit(userId)).thenThrow(new RuntimeException("error"));

        String result = controller.registerCarShare(carShareRegiId, userId);
        assertEquals("차량 공유 예약에 실패했습니다.", result);

        verify(registrationService).findLeftoverCredit(userId);
    }

    // 6. GET /rating/{memberId}
    @Test
    void testFindRating() {
        Long memberId = 1L;
        Double rating = 4.5;
        when(registrationService.findRating(memberId)).thenReturn(rating);

        Double result = controller.findRating(memberId);
        assertEquals(rating, result);
        verify(registrationService).findRating(memberId);
    }

    // 7. GET /checked-credit
    @Test
    void testFindLeftoverCredit_Positive() {
        Long userId = 1L;
        when(registrationService.findLeftoverCredit(userId)).thenReturn(10);

        ResponseEntity<String> response = controller.findLeftoverCredit(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("잔여 크레딧은 10입니다.", response.getBody());
        verify(registrationService).findLeftoverCredit(userId);
    }

    @Test
    void testFindLeftoverCredit_Zero() {
        Long userId = 1L;
        when(registrationService.findLeftoverCredit(userId)).thenReturn(0);

        ResponseEntity<String> response = controller.findLeftoverCredit(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("잔여 크레딧은 0 입니다.", response.getBody());
        verify(registrationService).findLeftoverCredit(userId);
    }

    @Test
    void testFindLeftoverCredit_RuntimeException() {
        Long userId = 1L;
        when(registrationService.findLeftoverCredit(userId)).thenThrow(new RuntimeException("error"));

        ResponseEntity<String> response = controller.findLeftoverCredit(userId);
        String expectedMessage = "(에러) " + userId + "번 회원의 크레딧 정보가 없습니다.";
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(expectedMessage, response.getBody());
        verify(registrationService).findLeftoverCredit(userId);
    }

    @Test
    void testFindLeftoverCredit_Exception() {
        Long userId = 1L;
        when(registrationService.findLeftoverCredit(userId)).thenThrow(new Exception("error"));

        ResponseEntity<String> response = controller.findLeftoverCredit(userId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(registrationService).findLeftoverCredit(userId);
    }

    // 8. POST /deducted-credit
    @Test
    void testInsertCreditByCar_SufficientCredit() {
        Long userId = 1L;
        when(registrationService.findLeftoverCredit(userId)).thenReturn(10);

        ResponseEntity<String> response = controller.insertCreditByCar(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("7 크레딧이 차감되었습니다.", response.getBody());
        verify(registrationService).findLeftoverCredit(userId);
        verify(registrationService).insertCreditByCar(userId);
    }

    @Test
    void testInsertCreditByCar_InsufficientCredit() {
        Long userId = 1L;
        when(registrationService.findLeftoverCredit(userId)).thenReturn(5);

        ResponseEntity<String> response = controller.insertCreditByCar(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("잔여 크레딧이 부족합니다.", response.getBody());
        verify(registrationService).findLeftoverCredit(userId);
        verify(registrationService, never()).insertCreditByCar(userId);
    }

    @Test
    void testInsertCreditByCar_RuntimeException() {
        Long userId = 1L;
        when(registrationService.findLeftoverCredit(userId)).thenThrow(new RuntimeException("error"));

        ResponseEntity<String> response = controller.insertCreditByCar(userId);
        String expectedMessage = "(에러) " + userId + "번 사용자에 대한 정보가 없습니다. error";
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(expectedMessage, response.getBody());
        verify(registrationService).findLeftoverCredit(userId);
    }

    @Test
    void testInsertCreditByCar_Exception() {
        Long userId = 1L;
        when(registrationService.findLeftoverCredit(userId)).thenThrow(new Exception("error"));

        ResponseEntity<String> response = controller.insertCreditByCar(userId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(registrationService).findLeftoverCredit(userId);
    }

    // 9. GET /all-list
    @Test
    void testFindAllShareCarList_Empty() {
        when(registrationService.findAllShareCarList()).thenReturn(Collections.emptyList());

        ResponseEntity<List<AllCarListResponse>> response = controller.findAllShareCarList();
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(registrationService).findAllShareCarList();
    }

    @Test
    void testFindAllShareCarList_Success() {
        List<AllCarListResponse> list = new ArrayList<>();
        list.add(allCarListResponse);
        when(registrationService.findAllShareCarList()).thenReturn(list);

        ResponseEntity<List<AllCarListResponse>> response = controller.findAllShareCarList();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(list, response.getBody());
        verify(registrationService).findAllShareCarList();
    }

    @Test
    void testFindAllShareCarList_Exception() {
        when(registrationService.findAllShareCarList()).thenThrow(new RuntimeException("error"));

        ResponseEntity<List<AllCarListResponse>> response = controller.findAllShareCarList();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(registrationService).findAllShareCarList();
    }

    // 10. GET /past-list
    @Test
    void testFindPastParticipationList_NullResponse() {
        Long userId = 1L;
        when(registrationService.findPastParticipationList(userId)).thenReturn(null);
        String expectedMessage = "(정보) " + userId + "번 사용자의 과거 탑승 내역이 없습니다.";

        ResponseEntity<Object> response = controller.findPastParticipationList(userId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(expectedMessage, response.getBody());
        verify(registrationService).findPastParticipationList(userId);
    }

    @Test
    void testFindPastParticipationList_Success() {
        Long userId = 1L;
        PastParticipationListResponse pastResponse = pastParticipationListResponse;
        when(registrationService.findPastParticipationList(userId)).thenReturn((List<PastParticipationListResponse>) pastResponse);

        ResponseEntity<Object> response = controller.findPastParticipationList(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pastResponse, response.getBody());
        verify(registrationService).findPastParticipationList(userId);
    }

    @Test
    void testFindPastParticipationList_RuntimeException() {
        Long userId = 1L;
        when(registrationService.findPastParticipationList(userId)).thenThrow(new RuntimeException("error"));
        String expectedMessage = "(정보) " + userId + "번 사용자의 과거 탑승 내역이 없습니다.";

        ResponseEntity<Object> response = controller.findPastParticipationList(userId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(expectedMessage, response.getBody());
        verify(registrationService).findPastParticipationList(userId);
    }

    @Test
    void testFindPastParticipationList_Exception() {
        Long userId = 1L;
        when(registrationService.findPastParticipationList(userId)).thenThrow(new Exception("error"));

        ResponseEntity<Object> response = controller.findPastParticipationList(userId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(registrationService).findPastParticipationList(userId);
    }

    // 11. GET /today-list
    @Test
    void testFindTodayParticipationList() {
        Long userId = 1L;
        List<PastParticipationListResponse> list = new ArrayList<>();
        list.add(pastParticipationListResponse);
        when(registrationService.findTodayParticipationList(userId)).thenReturn(list);

        ResponseEntity<List<PastParticipationListResponse>> response = controller.findTodayParticipationList(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(list, response.getBody());
        verify(registrationService).findTodayParticipationList(userId);
    }
}
