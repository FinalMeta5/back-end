package com.hifive.bururung.domain.taxi.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hifive.bururung.domain.taxi.dto.TaxiShareJoinRequest;
import com.hifive.bururung.domain.taxi.repository.ITaxiShareJoinRepository;
import com.hifive.bururung.global.exception.CustomException;
import com.hifive.bururung.global.exception.errorcode.TaxiShareJoinErrorCode;

@ExtendWith(MockitoExtension.class)
public class TaxiShareJoinServiceTest {

    @InjectMocks
    private TaxiShareJoinService taxiShareJoinService;
    
    @Mock
    private ITaxiShareJoinRepository taxiShareJoinRepository;
    
    // 1. getJoinCountByTaxiShareId() 성공 케이스
    @Test
    void testGetJoinCountByTaxiShareId_Success() {
        Long taxiShareId = 1L;
        when(taxiShareJoinRepository.getJoinCountByTaxiShareId(taxiShareId)).thenReturn(5);
        
        int result = taxiShareJoinService.getJoinCountByTaxiShareId(taxiShareId);
        assertEquals(5, result);
    }
    
    // 1. getJoinCountByTaxiShareId() 예외 케이스
    @Test
    void testGetJoinCountByTaxiShareId_Exception() {
        Long taxiShareId = 1L;
        when(taxiShareJoinRepository.getJoinCountByTaxiShareId(taxiShareId))
            .thenThrow(new RuntimeException("DB error"));
        
        CustomException exception = assertThrows(CustomException.class, () ->
            taxiShareJoinService.getJoinCountByTaxiShareId(taxiShareId)
        );
        assertEquals(TaxiShareJoinErrorCode.JOIN_NOT_FOUND, exception.getErrorCode());
    }
    
    // 2. insertTaxiShareJoin() 성공 케이스 (예외 발생하지 않음)
    @Test
    void testInsertTaxiShareJoin_Success() {
        TaxiShareJoinRequest joinRequest = mock(TaxiShareJoinRequest.class);
        // 아무런 예외도 발생하지 않는 상황 stubbing
        doNothing().when(taxiShareJoinRepository).insertTaxiShareJoin(joinRequest);
        
        // 호출 시 예외가 발생하지 않아야 함
        assertDoesNotThrow(() -> taxiShareJoinService.insertTaxiShareJoin(joinRequest));
        verify(taxiShareJoinRepository).insertTaxiShareJoin(joinRequest);
    }
    
    // 3. getDuplCntByTaxiShareIdAndMemberId()
    @Test
    void testGetDuplCntByTaxiShareIdAndMemberId() {
        TaxiShareJoinRequest joinRequest = mock(TaxiShareJoinRequest.class);
        when(taxiShareJoinRepository.getDuplCntByTaxiShareIdAndMemberId(joinRequest)).thenReturn(0);
        
        int result = taxiShareJoinService.getDuplCntByTaxiShareIdAndMemberId(joinRequest);
        assertEquals(0, result);
    }
    
    // 4. getMemberIdByTaxiShareId()
    @Test
    void testGetMemberIdByTaxiShareId() {
        Long taxiShareId = 2L;
        List<Long> memberIds = List.of(10L, 20L, 30L);
        when(taxiShareJoinRepository.getMemberIdByTaxiShareId(taxiShareId)).thenReturn(memberIds);
        
        List<Long> result = taxiShareJoinService.getMemberIdByTaxiShareId(taxiShareId);
        assertEquals(memberIds, result);
    }
    
    // 5. deleteTaxiShareJoinByTaxiShareId() 성공 케이스
    @Test
    void testDeleteTaxiShareJoinByTaxiShareId_Success() {
        Long taxiShareId = 3L;
        doNothing().when(taxiShareJoinRepository).deleteTaxiShareJoinById(taxiShareId);
        
        assertDoesNotThrow(() -> taxiShareJoinService.deleteTaxiShareJoinByTaxiShareId(taxiShareId));
        verify(taxiShareJoinRepository).deleteTaxiShareJoinById(taxiShareId);
    }
    
    // 5. deleteTaxiShareJoinByTaxiShareId() 예외 케이스
    @Test
    void testDeleteTaxiShareJoinByTaxiShareId_Exception() {
        Long taxiShareId = 3L;
        doThrow(new RuntimeException("Delete error")).when(taxiShareJoinRepository).deleteTaxiShareJoinById(taxiShareId);
        
        // 메서드 내에서 예외를 catch하므로, 호출 시 예외가 발생하지 않아야 함.
        assertDoesNotThrow(() -> taxiShareJoinService.deleteTaxiShareJoinByTaxiShareId(taxiShareId));
        verify(taxiShareJoinRepository).deleteTaxiShareJoinById(taxiShareId);
    }
    
    // 6. findLeftoverCredit()
    @Test
    void testFindLeftoverCredit() {
        Long memberId = 5L;
        when(taxiShareJoinRepository.findLeftoverCredit(memberId)).thenReturn(10);
        
        int result = taxiShareJoinService.findLeftoverCredit(memberId);
        assertEquals(10, result);
    }
    
    // 7. insertCreditByTaxi() 성공 케이스
    @Test
    void testInsertCreditByTaxi_Success() {
        Long memberId = 5L;
        int count = 3;
        when(taxiShareJoinRepository.findLeftoverCredit(memberId)).thenReturn(5);
        doNothing().when(taxiShareJoinRepository).insertCreditByTaxi(count, memberId);
        
        // 호출 시 예외 발생 없이 진행되어야 함.
        assertDoesNotThrow(() -> taxiShareJoinService.insertCreditByTaxi(count, memberId));
        verify(taxiShareJoinRepository).insertCreditByTaxi(count, memberId);
    }
    
    // 7. insertCreditByTaxi() 실패 케이스
    @Test
    void testInsertCreditByTaxi_Failure() {
        Long memberId = 5L;
        int count = 10;
        when(taxiShareJoinRepository.findLeftoverCredit(memberId)).thenReturn(5);
        
        CustomException exception = assertThrows(CustomException.class, () ->
            taxiShareJoinService.insertCreditByTaxi(count, memberId)
        );
        assertEquals(TaxiShareJoinErrorCode.CREDIT_DEDUCTED_FAILED, exception.getErrorCode());
        verify(taxiShareJoinRepository, never()).insertCreditByTaxi(anyInt(), anyLong());
    }
    
    // 8. getCarShareCountByMemberIdAndSysdate() 성공 케이스
    @Test
    void testGetCarShareCountByMemberIdAndSysdate_Success() {
        List<HashMap<String, Object>> expectedList = mock(List.class);
        when(taxiShareJoinRepository.getCarShareCountByMemberIdAndSysdate()).thenReturn(expectedList);
        
        List<HashMap<String, Object>> result = taxiShareJoinService.getCarShareCountByMemberIdAndSysdate();
        assertEquals(expectedList, result);
    }
    
    // 8. getCarShareCountByMemberIdAndSysdate() 예외 케이스
    @Test
    void testGetCarShareCountByMemberIdAndSysdate_Exception() {
        when(taxiShareJoinRepository.getCarShareCountByMemberIdAndSysdate())
            .thenThrow(new RuntimeException("Query error"));
        
        CustomException exception = assertThrows(CustomException.class, () ->
            taxiShareJoinService.getCarShareCountByMemberIdAndSysdate()
        );
        assertEquals(TaxiShareJoinErrorCode.CAR_SHARE_SYSDATE_NOT_FOUND, exception.getErrorCode());
    }
}
