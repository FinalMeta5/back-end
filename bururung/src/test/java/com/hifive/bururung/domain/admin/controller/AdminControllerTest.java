package com.hifive.bururung.domain.admin.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.hifive.bururung.domain.admin.dto.AdminCarRegistrationDTO;
import com.hifive.bururung.domain.admin.dto.AdminCarRegistrationRequest;
import com.hifive.bururung.domain.admin.dto.AdminCarRegistrationResponse;
import com.hifive.bururung.domain.admin.dto.AdminCarShareDTO;
import com.hifive.bururung.domain.admin.dto.AdminCarShareResponse;
import com.hifive.bururung.domain.admin.dto.AdminMemberDTO;
import com.hifive.bururung.domain.admin.dto.AdminMemberResponse;
import com.hifive.bururung.domain.admin.dto.AdminPaymentDTO;
import com.hifive.bururung.domain.admin.dto.AdminPaymentResponse;
import com.hifive.bururung.domain.admin.dto.AdminTaxiShareDTO;
import com.hifive.bururung.domain.admin.dto.AdminTaxiShareResponse;
import com.hifive.bururung.domain.admin.service.IAdminService;
import com.hifive.bururung.domain.notification.entity.Notification;
import com.hifive.bururung.domain.notification.service.INotificationService;

@ExtendWith(MockitoExtension.class)
public class AdminControllerTest {

    @InjectMocks
    private AdminController adminController;
    
    @Mock
    private IAdminService adminService;
    
    @Mock
    private INotificationService notificationService;
    
    @Test
    public void testGetMemberList_success() {
        // 더미 데이터 생성
        List<AdminMemberDTO> memberList = Arrays.asList(new AdminMemberDTO(), new AdminMemberDTO());
        when(adminService.getMemberList()).thenReturn(memberList);
        
        // 직접 메서드 호출
        ResponseEntity<AdminMemberResponse<List<AdminMemberDTO>>> responseEntity = adminController.getMemberList();
        AdminMemberResponse<List<AdminMemberDTO>> response = responseEntity.getBody();
        
        // 응답 검증
        assertNotNull(response);
        assertTrue(response.isStatus());
        assertEquals(memberList.size(), response.getData().size());
    }
    
    @Test
    public void testGetRegistrationList_success() {
        List<AdminCarRegistrationDTO> registrationList = Arrays.asList(new AdminCarRegistrationDTO());
        when(adminService.getRegistrationList()).thenReturn(registrationList);
        
        ResponseEntity<AdminCarRegistrationResponse<List<AdminCarRegistrationDTO>>> responseEntity = adminController.getRegistrationList();
        AdminCarRegistrationResponse<List<AdminCarRegistrationDTO>> response = responseEntity.getBody();
        
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(registrationList.size(), response.getData().size());
    }
    
    @Test
    public void testGetTaxiShareServiceList_success() {
        List<AdminTaxiShareDTO> taxiShareList = Arrays.asList(new AdminTaxiShareDTO());
        when(adminService.getTaxiShareServiceList()).thenReturn(taxiShareList);
        
        ResponseEntity<AdminTaxiShareResponse<List<AdminTaxiShareDTO>>> responseEntity = adminController.getTaxiShareServiceList();
        AdminTaxiShareResponse<List<AdminTaxiShareDTO>> response = responseEntity.getBody();
        
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(taxiShareList.size(), response.getData().size());
    }
    
    @Test
    public void testGetCarShareServiceList_success() {
        List<AdminCarShareDTO> carShareList = Arrays.asList(new AdminCarShareDTO());
        when(adminService.getCarShareServiceList()).thenReturn(carShareList);
        
        ResponseEntity<AdminCarShareResponse<List<AdminCarShareDTO>>> responseEntity = adminController.getCarShareServiceList();
        AdminCarShareResponse<List<AdminCarShareDTO>> response = responseEntity.getBody();
        
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(carShareList.size(), response.getData().size());
    }
    
    @Test
    public void testGetPaymentList_success() {
        List<AdminPaymentDTO> paymentList = Arrays.asList(new AdminPaymentDTO());
        when(adminService.getPaymentList()).thenReturn(paymentList);
        
        ResponseEntity<AdminPaymentResponse<List<AdminPaymentDTO>>> responseEntity = adminController.getPaymentList();
        AdminPaymentResponse<List<AdminPaymentDTO>> response = responseEntity.getBody();
        
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(paymentList.size(), response.getData().size());
    }
    
    @Test
    public void testApproveRegistration_success() {
        AdminCarRegistrationRequest request = new AdminCarRegistrationRequest();
        // 필요한 경우 request 필드 설정
        
        // adminService.approveRegistration 호출 시 아무런 예외 없이 실행됨을 가정
        doNothing().when(adminService).approveRegistration(any(AdminCarRegistrationRequest.class));
        
        ResponseEntity<String> responseEntity = adminController.approveRegistration(request);
        
        assertEquals("ok", responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
    
    @Test
    public void testRequestUpdateRegistration_success() {
        AdminCarRegistrationRequest request = new AdminCarRegistrationRequest();
        request.setCarId(1L);
        request.setMemberId(2L);
        
        // deleteRegistration, sendNotification 호출 시 예외 없이 동작하도록 설정
        doNothing().when(adminService).deleteRegistration(anyLong());
        doNothing().when(notificationService).sendNotification(any(Notification.class));
        
        ResponseEntity<String> responseEntity = adminController.requestUpdateRegistration(request);
        
        assertEquals("ok", responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}
