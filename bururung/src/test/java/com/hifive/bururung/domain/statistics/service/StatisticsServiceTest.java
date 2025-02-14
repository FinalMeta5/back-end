package com.hifive.bururung.domain.statistics.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hifive.bururung.domain.statistics.dto.AgeGroupRatioResponse;
import com.hifive.bururung.domain.statistics.dto.GenderRatioResponse;
import com.hifive.bururung.domain.statistics.dto.MemberNameWithRateResponse;
import com.hifive.bururung.domain.statistics.dto.MonthlyNewMemberResponse;
import com.hifive.bururung.domain.statistics.repository.IStatisticsMapper;

@ExtendWith(MockitoExtension.class)
public class StatisticsServiceTest {

    @InjectMocks
    private StatisticsService statisticsService;
    
    @Mock
    private IStatisticsMapper iStatisticsMapper;
    
    // 도메인 객체들은 new 대신 @Mock을 사용하여 생성
    @Mock
    private GenderRatioResponse genderResponse1;
    
    @Mock
    private GenderRatioResponse genderResponse2;
    
    @Mock
    private AgeGroupRatioResponse ageGroupResponse;
    
    @Mock
    private MonthlyNewMemberResponse monthlyNewMemberResponse;
    
    @Mock
    private MemberNameWithRateResponse memberNameWithRateResponse;
    
    // 1. getGenderRatio() 테스트
    @Test
    void testGetGenderRatio() {
        // stubbing: 첫번째 객체: 남자, 60건, 두번째 객체: 여자, 40건
        when(genderResponse1.getCount()).thenReturn(60);
        when(genderResponse1.getGender()).thenReturn("M");
        
        when(genderResponse2.getCount()).thenReturn(40);
        when(genderResponse2.getGender()).thenReturn("F");
        
        // iStatisticsMapper.getGenderCount()가 위 두 객체를 포함하는 리스트를 반환하도록 stubbing
        when(iStatisticsMapper.getGenderCount()).thenReturn(List.of(genderResponse1, genderResponse2));
        
        // 호출
        List<GenderRatioResponse> result = statisticsService.getGenderRatio();
        
        // 총합: 100
        assertEquals(2, result.size());
        GenderRatioResponse res1 = result.get(0);
        GenderRatioResponse res2 = result.get(1);
        
        // 첫번째 객체: "M" → "남자", 계산: 60 * 100 / 100 = 60
        assertEquals(60, res1.getCount());
        assertEquals("남자", res1.getGender());
        
        // 두번째 객체: "F" → "여자", 계산: 40 * 100 / 100 = 40
        assertEquals(40, res2.getCount());
        assertEquals("여자", res2.getGender());
    }
    
    // 2. getAgeGroupRatio() 테스트
    @Test
    void testGetAgeGroupRatio() {
        List<AgeGroupRatioResponse> expected = List.of(ageGroupResponse);
        when(iStatisticsMapper.getAgeGroupRatio()).thenReturn(expected);
        
        List<AgeGroupRatioResponse> result = statisticsService.getAgeGroupRatio();
        assertEquals(expected, result);
    }
    
    // 3. getMonthlyNewMemberCount() 테스트
    @Test
    void testGetMonthlyNewMemberCount() {
        List<MonthlyNewMemberResponse> expected = List.of(monthlyNewMemberResponse);
        when(iStatisticsMapper.getMonthlyNewMemberCount()).thenReturn(expected);
        
        List<MonthlyNewMemberResponse> result = statisticsService.getMonthlyNewMemberCount();
        assertEquals(expected, result);
    }
    
    // 4. getMembersByRatingDesc() 테스트
    @Test
    void testGetMembersByRatingDesc() {
        List<MemberNameWithRateResponse> expected = List.of(memberNameWithRateResponse);
        when(iStatisticsMapper.getMembersByRatingDesc()).thenReturn(expected);
        
        List<MemberNameWithRateResponse> result = statisticsService.getMembersByRatingDesc();
        assertEquals(expected, result);
    }
}
