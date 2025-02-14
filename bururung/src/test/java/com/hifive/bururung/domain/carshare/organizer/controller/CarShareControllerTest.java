package com.hifive.bururung.domain.carshare.organizer.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import com.hifive.bururung.domain.carshare.organizer.dto.CarShareRegiRequestDTO;
import com.hifive.bururung.domain.carshare.organizer.entity.CarShareRegistration;
import com.hifive.bururung.domain.carshare.organizer.repository.CarRegistrationRepository;
import com.hifive.bururung.domain.carshare.organizer.service.ICarShareService;

@ExtendWith(MockitoExtension.class)
public class CarShareControllerTest {

    @InjectMocks
    private CarShareController controller;

    @Mock
    private ICarShareService carShareService;

    @Mock
    private CarRegistrationRepository carRegistrationRepository;
    
    @Mock
    private Authentication authentication;

    // ──────────────── registerCarShare ────────────────

    // 1. 요청 데이터가 null인 경우
    @Test
    void testRegisterCarShare_NullRequest() throws Exception {
        ResponseEntity<String> response = controller.registerCarShare(null, authentication);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("요청 데이터가 없습니다.", response.getBody());
    }

    // 2. 로그인한 회원의 차량 정보가 없는 경우 (repository가 null을 반환)
    @Test
    void testRegisterCarShare_NoCarFound() throws Exception {
        // 준비: 유효한 요청 DTO 생성
        CarShareRegiRequestDTO request = new CarShareRegiRequestDTO();
        request.setPickupDate("2025-02-14T06:00");

        // Authentication에서 회원 ID를 "1"로 반환하도록 설정
        when(authentication.getName()).thenReturn("1");
        // repository에서 차량 ID가 없음을 의미하도록 null 반환
        when(carRegistrationRepository.findCarIdByMemberId(1L)).thenReturn(null);

        ResponseEntity<String> response = controller.registerCarShare(request, authentication);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("🚨 해당 회원의 차량 정보가 없습니다.", response.getBody());
    }

    // 3. pickupDate 포맷이 올바르지 않아 파싱에 실패하는 경우
    @Test
    void testRegisterCarShare_InvalidPickupDate() throws Exception {
        CarShareRegiRequestDTO request = new CarShareRegiRequestDTO();
        request.setPickupDate("invalid-date"); // ISO_LOCAL_DATE_TIME 포맷이 아님

        when(authentication.getName()).thenReturn("1");
        when(carRegistrationRepository.findCarIdByMemberId(1L)).thenReturn(100L);

        // LocalDateTime.parse() 호출 시 DateTimeParseException 발생 예상
        assertThrows(Exception.class, () -> controller.registerCarShare(request, authentication));
    }

    // 4. 정상 등록되는 경우
    @Test
    void testRegisterCarShare_Success() throws Exception {
        CarShareRegiRequestDTO request = new CarShareRegiRequestDTO();
        request.setPickupDate("2025-02-14T06:00");

        when(authentication.getName()).thenReturn("1");
        when(carRegistrationRepository.findCarIdByMemberId(1L)).thenReturn(100L);

        // pickupDate 문자열을 파싱하여 기대하는 LocalDateTime 객체 생성
        LocalDateTime expectedPickupDate = LocalDateTime.parse(request.getPickupDate(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        // 등록 후 반환할 엔티티 생성 (ID 값 설정)
        CarShareRegistration savedCarShare = new CarShareRegistration();
        savedCarShare.setCarShareRegiId(200L);

        when(carShareService.registerCarShare(request, 1L, 100L, expectedPickupDate))
                .thenReturn(savedCarShare);

        ResponseEntity<String> response = controller.registerCarShare(request, authentication);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("차량 공유 서비스 등록 성공 (ID: 200)", response.getBody());
    }

    // ──────────────── getMyCarShares ────────────────

    // 5. 내 차량 공유 서비스 목록이 없을 경우 (empty list → 204 No Content)
    @Test
    void testGetMyCarShares_NoContent() {
        when(authentication.getName()).thenReturn("1");
        when(carShareService.getAllCarSharesByMemberId(1L)).thenReturn(new ArrayList<>());

        ResponseEntity<List<CarShareRegistration>> response = controller.getMyCarShares(authentication);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    // 6. 내 차량 공유 서비스 목록이 있을 경우
    @Test
    void testGetMyCarShares_Success() {
        when(authentication.getName()).thenReturn("1");
        List<CarShareRegistration> list = new ArrayList<>();
        CarShareRegistration reg = new CarShareRegistration();
        reg.setCarShareRegiId(300L);
        list.add(reg);

        when(carShareService.getAllCarSharesByMemberId(1L)).thenReturn(list);

        ResponseEntity<List<CarShareRegistration>> response = controller.getMyCarShares(authentication);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(list, response.getBody());
    }
}

