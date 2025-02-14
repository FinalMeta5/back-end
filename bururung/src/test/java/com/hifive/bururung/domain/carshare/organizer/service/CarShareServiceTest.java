package com.hifive.bururung.domain.carshare.organizer.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hifive.bururung.domain.carshare.organizer.dto.CarShareRegiRequestDTO;
import com.hifive.bururung.domain.carshare.organizer.entity.CarShareRegistration;
import com.hifive.bururung.domain.carshare.organizer.repository.CarShareRepository;

@ExtendWith(MockitoExtension.class)
public class CarShareServiceTest {

    @InjectMocks
    private CarShareService carShareService;

    @Mock
    private CarShareRepository carShareRepository;

    // ────────────── registerCarShare ──────────────

    @Test
    void testRegisterCarShare_Success() {
        // 준비: DTO 생성 및 필드 세팅
        CarShareRegiRequestDTO request = new CarShareRegiRequestDTO();
        request.setPickupLoc("Location A");
        request.setLatitudePl(37.12345);
        request.setLongitudePl(127.12345);
        request.setSidoPl("Seoul");
        request.setSigunguPl("Gangnam-gu");
        request.setRoadnamePl("Gangnam-daero");
        request.setDestination("Destination B");
        request.setLatitudeDs(37.54321);
        request.setLongitudeDs(127.54321);
        request.setSidoDs("Busan");
        request.setSigunguDs("Haeundae-gu");
        request.setRoadnameDs("Haeundae-ro");
        request.setPassengersNum(4);
        request.setCategory("Standard");

        Long memberId = 1L;
        Long carId = 100L;
        LocalDateTime pickupDateTime = LocalDateTime.of(2025, 2, 14, 6, 0);

        // repository.save() 호출 시 인자로 전달된 엔티티를 그대로 반환하도록 설정
        when(carShareRepository.save(any(CarShareRegistration.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        // 메서드 호출
        CarShareRegistration result = carShareService.registerCarShare(request, memberId, carId, pickupDateTime);

        // 검증: 각 필드가 DTO와 입력값에 따라 올바르게 세팅되었는지 확인
        assertNotNull(result);
        assertEquals(memberId, result.getMemberId());
        assertEquals(carId, result.getCarId());
        assertEquals(request.getPickupLoc(), result.getPickupLoc());
        assertEquals(request.getLatitudePl(), result.getLatitudePl());
        assertEquals(request.getLongitudePl(), result.getLongitudePl());
        assertEquals(request.getSidoPl(), result.getSidoPl());
        assertEquals(request.getSigunguPl(), result.getSigunguPl());
        // 빌더로 생성 시 roadname 필드 이름이 다를 수 있으므로 실제 getter 명칭에 맞게 수정 필요
        assertEquals(request.getRoadnamePl(), result.getRoadNamePl());
        assertEquals(request.getDestination(), result.getDestination());
        assertEquals(request.getLatitudeDs(), result.getLatitudeDs());
        assertEquals(request.getLongitudeDs(), result.getLongitudeDs());
        assertEquals(request.getSidoDs(), result.getSidoDs());
        assertEquals(request.getSigunguDs(), result.getSigunguDs());
        // 빌더로 생성 시 roadname 필드 이름이 다를 수 있으므로 실제 getter 명칭에 맞게 수정 필요
        assertEquals(request.getRoadnameDs(), result.getRoadNameDs());
        assertEquals(request.getPassengersNum(), result.getPassengersNum());
        assertEquals(pickupDateTime, result.getPickupDate());
        assertEquals(request.getCategory(), result.getCategory());

        // repository.save() 호출 여부 검증
        verify(carShareRepository).save(any(CarShareRegistration.class));
    }

    // ────────────── getAllCarSharesByMemberId ──────────────

    @Test
    void testGetAllCarSharesByMemberId_Success() {
        Long memberId = 1L;
        List<CarShareRegistration> dummyList = new ArrayList<>();
        CarShareRegistration reg1 = new CarShareRegistration();
        reg1.setCarShareRegiId(10L);
        dummyList.add(reg1);
        CarShareRegistration reg2 = new CarShareRegistration();
        reg2.setCarShareRegiId(20L);
        dummyList.add(reg2);

        when(carShareRepository.findByMemberId(memberId)).thenReturn(dummyList);

        List<CarShareRegistration> result = carShareService.getAllCarSharesByMemberId(memberId);
        assertNotNull(result);
        assertEquals(dummyList.size(), result.size());
        assertEquals(dummyList, result);

        verify(carShareRepository).findByMemberId(memberId);
    }
}
