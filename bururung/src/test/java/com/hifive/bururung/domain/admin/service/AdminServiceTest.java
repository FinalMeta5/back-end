package com.hifive.bururung.domain.admin.service;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hifive.bururung.domain.admin.dto.AdminCarRegistrationDTO;
import com.hifive.bururung.domain.admin.dto.AdminCarRegistrationRequest;
import com.hifive.bururung.domain.admin.dto.AdminCarShareDTO;
import com.hifive.bururung.domain.admin.dto.AdminMemberDTO;
import com.hifive.bururung.domain.admin.dto.AdminPaymentDTO;
import com.hifive.bururung.domain.admin.dto.AdminTaxiShareDTO;
import com.hifive.bururung.domain.admin.repository.IAdminMapper;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

    @InjectMocks
    private AdminService adminService;

    @Mock
    private IAdminMapper adminMapper;

    @Test
    public void testGetMemberList() {
        // given
        List<AdminMemberDTO> expectedList = new ArrayList<>();
        when(adminMapper.getMemberList()).thenReturn(expectedList);

        // when
        List<AdminMemberDTO> actualList = adminService.getMemberList();

        // then
        assertSame(expectedList, actualList);
        verify(adminMapper).getMemberList();
    }

    @Test
    public void testGetRegistrationList() {
        // given
        List<AdminCarRegistrationDTO> expectedList = new ArrayList<>();
        when(adminMapper.getRegistrationList()).thenReturn(expectedList);

        // when
        List<AdminCarRegistrationDTO> actualList = adminService.getRegistrationList();

        // then
        assertSame(expectedList, actualList);
        verify(adminMapper).getRegistrationList();
    }

    @Test
    public void testGetTaxiShareServiceList() {
        // given
        List<AdminTaxiShareDTO> expectedList = new ArrayList<>();
        when(adminMapper.getTaxiShareServiceList()).thenReturn(expectedList);

        // when
        List<AdminTaxiShareDTO> actualList = adminService.getTaxiShareServiceList();

        // then
        assertSame(expectedList, actualList);
        verify(adminMapper).getTaxiShareServiceList();
    }

    @Test
    public void testGetCarShareServiceList() {
        // given
        List<AdminCarShareDTO> expectedList = new ArrayList<>();
        when(adminMapper.getCarShareServiceList()).thenReturn(expectedList);

        // when
        List<AdminCarShareDTO> actualList = adminService.getCarShareServiceList();

        // then
        assertSame(expectedList, actualList);
        verify(adminMapper).getCarShareServiceList();
    }

    @Test
    public void testGetPaymentList() {
        // given
        List<AdminPaymentDTO> expectedList = new ArrayList<>();
        when(adminMapper.getPaymentList()).thenReturn(expectedList);

        // when
        List<AdminPaymentDTO> actualList = adminService.getPaymentList();

        // then
        assertSame(expectedList, actualList);
        verify(adminMapper).getPaymentList();
    }

    @Test
    public void testApproveRegistration() {
        // given
        AdminCarRegistrationRequest registrationRequest = new AdminCarRegistrationRequest();
        Long dummyCarId = 123L;
        Long dummyMemberId = 456L;
        // registrationRequest에 carId와 memberId를 설정 (setter가 있다고 가정)
        registrationRequest.setCarId(dummyCarId);
        registrationRequest.setMemberId(dummyMemberId);

        // when
        adminService.approveRegistration(registrationRequest);

        // then
        verify(adminMapper).updateRegistrationSetVerifiedToY(dummyCarId);
        verify(adminMapper).updateMemberToDriver(dummyMemberId);
    }

    @Test
    public void testDeleteRegistration() {
        // given
        Long carId = 789L;

        // when
        adminService.deleteRegistration(carId);

        // then
        verify(adminMapper).deleteRegistration(carId);
    }
}
