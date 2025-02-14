package com.hifive.bururung.domain.statistics.controller;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import java.util.Collections;
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
import com.hifive.bururung.domain.statistics.service.IStatisticsService;

@ExtendWith(MockitoExtension.class)
public class StatisticsControllerTest {

    @InjectMocks
    private StatisticsController statisticsController;

    @Mock
    private IStatisticsService iStatisticsService;

    @Test
    void testGetGenderRatio() {
        // given
        List<GenderRatioResponse> dummyList = Collections.emptyList();
        when(iStatisticsService.getGenderRatio()).thenReturn(dummyList);

        // when
        List<GenderRatioResponse> result = statisticsController.getGenderRatio();

        // then
        assertSame(dummyList, result);
    }

    @Test
    void testGetAgeGroupRatio() {
        // given
        List<AgeGroupRatioResponse> dummyList = Collections.emptyList();
        when(iStatisticsService.getAgeGroupRatio()).thenReturn(dummyList);

        // when
        List<AgeGroupRatioResponse> result = statisticsController.getAgeGroupRatio();

        // then
        assertSame(dummyList, result);
    }

    @Test
    void testGetMonthlyNewMemberCount() {
        // given
        List<MonthlyNewMemberResponse> dummyList = Collections.emptyList();
        when(iStatisticsService.getMonthlyNewMemberCount()).thenReturn(dummyList);

        // when
        List<MonthlyNewMemberResponse> result = statisticsController.getMonthlyNewMemberCount();

        // then
        assertSame(dummyList, result);
    }

    @Test
    void testGetMembersByRatingDesc() {
        // given
        List<MemberNameWithRateResponse> dummyList = Collections.emptyList();
        when(iStatisticsService.getMembersByRatingDesc()).thenReturn(dummyList);

        // when
        List<MemberNameWithRateResponse> result = statisticsController.getMembersByRatingDesc();

        // then
        assertSame(dummyList, result);
    }
}
