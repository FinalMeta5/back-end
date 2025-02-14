package com.hifive.bururung.domain.taxi.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hifive.bururung.domain.taxi.dto.TaxiShareJoinRequest;
import com.hifive.bururung.domain.taxi.dto.TaxiShareResponse;
import com.hifive.bururung.domain.taxi.entity.TaxiShare;
import com.hifive.bururung.domain.taxi.repository.ITaxiShareJoinRepository;
import com.hifive.bururung.domain.taxi.repository.ITaxiShareRepository;
import com.hifive.bururung.global.exception.CustomException;
import com.hifive.bururung.global.exception.errorcode.TaxiShareErrorCode;

@ExtendWith(MockitoExtension.class)
public class TaxiShareServiceTest {

    @InjectMocks
    private TaxiShareService taxiShareService;
    
    @Mock
    private ITaxiShareRepository taxiShareRepository;
    
    @Mock
    private ITaxiShareJoinRepository taxiShareJoinRepository;
    
    // 1. findAll() 테스트
    @Test
    void testFindAll() {
        List<TaxiShare> taxiShareList = mock(List.class);
        when(taxiShareRepository.findAll()).thenReturn(taxiShareList);
        
        List<TaxiShare> result = taxiShareService.findAll();
        assertEquals(taxiShareList, result);
    }
    
    // 2. insertTaxiShare() 테스트
    @Test
    void testInsertTaxiShare_Success() {
        // TaxiShare 도메인 객체는 @Mock으로 생성
        TaxiShare taxiShare = mock(TaxiShare.class);
        doNothing().when(taxiShareRepository).insertTaxiShare(taxiShare);
        
        // 예외 없이 호출되어야 함
        assertDoesNotThrow(() -> taxiShareService.insertTaxiShare(taxiShare));
        verify(taxiShareRepository).insertTaxiShare(taxiShare);
    }
    
    // 3. getTaxiShareByPickupTime() 테스트
    @Test
    void testGetTaxiShareByPickupTime() {
        String pickupTime = "2025-02-14T06:00";
        List<TaxiShareResponse> responseList = mock(List.class);
        when(taxiShareRepository.getTaxiShareByPickupTime(pickupTime)).thenReturn(responseList);
        
        List<TaxiShareResponse> result = taxiShareService.getTaxiShareByPickupTime(pickupTime);
        assertEquals(responseList, result);
    }
    
    // 4. getTaxiShareById() 테스트
    @Test
    void testGetTaxiShareById() {
        Long taxiShareId = 100L;
        TaxiShareResponse response = mock(TaxiShareResponse.class);
        when(taxiShareRepository.getTaxiShareById(taxiShareId)).thenReturn(response);
        
        TaxiShareResponse result = taxiShareService.getTaxiShareById(taxiShareId);
        assertEquals(response, result);
    }
    
    // 5. getCountTaxsiShareByIdAndMemberId() 테스트
    @Test
    void testGetCountTaxsiShareByIdAndMemberId() {
        TaxiShareJoinRequest joinRequest = mock(TaxiShareJoinRequest.class);
        when(taxiShareRepository.getCountTaxsiShareByIdAndMemberId(joinRequest)).thenReturn(3);
        
        int result = taxiShareService.getCountTaxsiShareByIdAndMemberId(joinRequest);
        assertEquals(3, result);
    }
    
    // 6. deleteTaxiShare() 테스트 - 성공 케이스
    @Test
    void testDeleteTaxiShare_Success() {
        TaxiShareJoinRequest joinRequest = mock(TaxiShareJoinRequest.class);
        Long taxiShareId = 200L;
        when(joinRequest.getTaxiShareId()).thenReturn(taxiShareId);
        doNothing().when(taxiShareJoinRepository).deleteTaxiShareJoinByTaxiShareId(taxiShareId);
        when(taxiShareRepository.deleteTaxiShare(joinRequest)).thenReturn(1);
        
        int result = taxiShareService.deleteTaxiShare(joinRequest);
        assertEquals(1, result);
        verify(taxiShareJoinRepository).deleteTaxiShareJoinByTaxiShareId(taxiShareId);
        verify(taxiShareRepository).deleteTaxiShare(joinRequest);
    }
    
    // 6. deleteTaxiShare() 테스트 - 예외 케이스
    @Test
    void testDeleteTaxiShare_Exception() {
        TaxiShareJoinRequest joinRequest = mock(TaxiShareJoinRequest.class);
        Long taxiShareId = 200L;
        when(joinRequest.getTaxiShareId()).thenReturn(taxiShareId);
        doNothing().when(taxiShareJoinRepository).deleteTaxiShareJoinByTaxiShareId(taxiShareId);
        when(taxiShareRepository.deleteTaxiShare(joinRequest)).thenThrow(new RuntimeException("Delete error"));
        
        CustomException exception = assertThrows(CustomException.class, () -> 
            taxiShareService.deleteTaxiShare(joinRequest)
        );
        assertEquals(TaxiShareErrorCode.TAXI_SHARE_DELETE_FAILED, exception.getErrorCode());
    }
}
