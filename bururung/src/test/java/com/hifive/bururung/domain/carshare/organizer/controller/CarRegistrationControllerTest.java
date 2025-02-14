package com.hifive.bururung.domain.carshare.organizer.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.hifive.bururung.domain.carshare.organizer.dto.CarUpdateRequestDTO;
import com.hifive.bururung.domain.carshare.organizer.dto.CarUpdateResponseDTO;
import com.hifive.bururung.domain.carshare.organizer.entity.CarRegistration;
import com.hifive.bururung.domain.carshare.organizer.service.ICarRegistrationService;
import com.hifive.bururung.domain.member.entity.Member;
import com.hifive.bururung.domain.member.service.MemberService;
import com.hifive.bururung.global.common.s3.FileSubPath;
import com.hifive.bururung.global.common.s3.S3Uploader;
import com.hifive.bururung.global.common.s3.UploadFileDTO;
import com.hifive.bururung.global.exception.CustomException;
import com.hifive.bururung.global.exception.errorcode.CarRegistrationErrorCode;
import com.hifive.bururung.global.util.SecurityUtil;

@ExtendWith(MockitoExtension.class)
public class CarRegistrationControllerTest {

    @InjectMocks
    private CarRegistrationController controller;
    
    @Mock
    private ICarRegistrationService carRegistrationService;
    
    @Mock
    private MemberService memberService;
    
    @Mock
    private S3Uploader s3Uploader;
    
    @BeforeEach
    void setUp() {
        // 특별한 설정이 없다면 이곳에 작성
    }
    
    // 1. POST /api/car-registration/upload-car-image
    @Test
    void testUploadCarImage_NullFile() throws IOException {
        ResponseEntity<String> response = controller.uploadCarImage(null);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("🚨 업로드할 파일이 없습니다! (NULL)", response.getBody());
    }
    
    @Test
    void testUploadCarImage_EmptyFile() throws IOException {
        MultipartFile emptyFile = new MockMultipartFile("carImage", "empty.jpg", "image/jpeg", new byte[0]);
        ResponseEntity<String> response = controller.uploadCarImage(emptyFile);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("🚨 업로드할 파일이 없습니다! (EMPTY)", response.getBody());
    }
    
    @Test
    void testUploadCarImage_Success() throws IOException {
        MultipartFile file = new MockMultipartFile("carImage", "test.jpg", "image/jpeg", "dummy".getBytes());
        UploadFileDTO uploadDTO = mock(UploadFileDTO.class);
        when(uploadDTO.getStoreFullUrl()).thenReturn("http://s3.example.com/test.jpg");
        when(s3Uploader.uploadFile(file, FileSubPath.CAR.getPath())).thenReturn(uploadDTO);
        
        ResponseEntity<String> response = controller.uploadCarImage(file);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("http://s3.example.com/test.jpg", response.getBody());
    }
    
    // 2. POST /api/car-registration/upload-verified-file
    @Test
    void testUploadAgreementFile_Success() throws IOException {
        MultipartFile file = new MockMultipartFile("agreementFile", "agreement.pdf", "application/pdf", "dummy".getBytes());
        UploadFileDTO uploadDTO = mock(UploadFileDTO.class);
        when(uploadDTO.getStoreFullUrl()).thenReturn("http://s3.example.com/agreement.pdf");
        when(s3Uploader.uploadFile(file, FileSubPath.VERIFIED.getPath())).thenReturn(uploadDTO);
        
        ResponseEntity<String> response = controller.uploadAgreementFile(file);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("http://s3.example.com/agreement.pdf", response.getBody());
    }
    
    // 3. POST /api/car-registration/register
    @Test
    void testRegisterCar_Success() {
        try (MockedStatic<SecurityUtil> secMock = mockStatic(SecurityUtil.class)) {
            secMock.when(SecurityUtil::getCurrentMemberId).thenReturn(1L);
            
            // Duplicate checks return false
            when(carRegistrationService.isCarAlreadyRegistered(1L)).thenReturn(false);
            when(carRegistrationService.isCarNumberExists("ABC123")).thenReturn(false);
            
            // Stub memberService.findByMemberId
            Member member = mock(Member.class);
            when(memberService.findByMemberId(1L)).thenReturn(member);
            
            // Stub registerCar() to do nothing
            doNothing().when(carRegistrationService).registerCar(any(CarRegistration.class));
            
            ResponseEntity<String> response = controller.registerCar(
                "http://s3.example.com/car.jpg",
                "http://s3.example.com/agreement.pdf",
                " ABC 123 ",
                "ModelX",
                4,
                "Red",
                "Nice car"
            );
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("차량이 성공적으로 등록되었습니다.", response.getBody());
            
            // Verify that car number was cleaned (i.e., spaces removed)
            ArgumentCaptor<CarRegistration> captor = ArgumentCaptor.forClass(CarRegistration.class);
            verify(carRegistrationService).registerCar(captor.capture());
            CarRegistration savedCar = captor.getValue();
            assertEquals("ABC123", savedCar.getCarNumber());
            assertEquals("ModelX", savedCar.getCarModel());
            assertEquals(4, savedCar.getMaxPassengers());
            assertEquals("Red", savedCar.getColor());
            assertEquals("Nice car", savedCar.getCarDescription());
        }
    }
    
    @Test
    void testRegisterCar_Unauthorized() {
        try (MockedStatic<SecurityUtil> secMock = mockStatic(SecurityUtil.class)) {
            String errorMsg = "Unauthorized access";
            secMock.when(SecurityUtil::getCurrentMemberId).thenThrow(new IllegalArgumentException(errorMsg));
            
            ResponseEntity<String> response = controller.registerCar(
                "url", "agreement", "123", "model", 4, "color", "desc"
            );
            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
            assertEquals(errorMsg, response.getBody());
        }
    }
    
    @Test
    void testRegisterCar_DuplicateRegistration() {
        try (MockedStatic<SecurityUtil> secMock = mockStatic(SecurityUtil.class)) {
            secMock.when(SecurityUtil::getCurrentMemberId).thenReturn(1L);
            when(carRegistrationService.isCarAlreadyRegistered(1L)).thenReturn(true);
            
            CustomException ex = assertThrows(CustomException.class, () ->
                controller.registerCar("url", "agreement", "123", "model", 4, "color", "desc")
            );
            assertEquals(CarRegistrationErrorCode.CAR_ALREADY_REGISTERED, ex.getErrorCode());
        }
    }
    
    @Test
    void testRegisterCar_DuplicateCarNumber() {
        try (MockedStatic<SecurityUtil> secMock = mockStatic(SecurityUtil.class)) {
            secMock.when(SecurityUtil::getCurrentMemberId).thenReturn(1L);
            when(carRegistrationService.isCarAlreadyRegistered(1L)).thenReturn(false);
            when(carRegistrationService.isCarNumberExists("ABC123")).thenReturn(true);
            
            CustomException ex = assertThrows(CustomException.class, () ->
                controller.registerCar("url", "agreement", " ABC 123 ", "model", 4, "color", "desc")
            );
            assertEquals(CarRegistrationErrorCode.DUPLICATE_CAR_NUMBER, ex.getErrorCode());
        }
    }
    
    // 4. GET /api/car-registration/list
    @Test
    void testGetAllCars() {
        List<CarRegistration> carList = mock(List.class);
        when(carRegistrationService.getAllCars()).thenReturn(carList);
        
        ResponseEntity<List<CarRegistration>> response = controller.getAllCars();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(carList, response.getBody());
    }
    
    // 5. GET /api/car-registration/car/by-number/{carNumber}
    @Test
    void testGetCarByCarNumber_Found() {
        String carNumber = "ABC123";
        CarRegistration car = mock(CarRegistration.class);
        when(carRegistrationService.getCarByCarNumber(carNumber)).thenReturn(Optional.of(car));
        
        ResponseEntity<CarRegistration> response = controller.getCarByCarNimber(carNumber);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(car, response.getBody());
    }
    
    @Test
    void testGetCarByCarNumber_NotFound() {
        String carNumber = "XYZ789";
        when(carRegistrationService.getCarByCarNumber(carNumber)).thenReturn(Optional.empty());
        
        ResponseEntity<CarRegistration> response = controller.getCarByCarNimber(carNumber);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
    
    // 6. GET /api/car-registration/car/by-id/{carId}
    @Test
    void testGetCarByCarId_Success() {
        Long carId = 10L;
        CarRegistration car = mock(CarRegistration.class);
        when(carRegistrationService.getCarByCarId(carId)).thenReturn(Optional.of(car));
        
        ResponseEntity<CarRegistration> response = controller.getCarByCarId(carId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(car, response.getBody());
    }
    
    @Test
    void testGetCarByCarId_NotFound() {
        Long carId = 10L;
        when(carRegistrationService.getCarByCarId(carId)).thenReturn(Optional.empty());
        
        CustomException ex = assertThrows(CustomException.class, () ->
            controller.getCarByCarId(carId)
        );
        assertEquals(CarRegistrationErrorCode.CAR_NOT_FOUND, ex.getErrorCode());
    }
    
    // 7. GET /api/car-registration/verified/{memberId}
    @Test
    void testIsCarVerified_Success() {
        Long memberId = 1L;
        when(carRegistrationService.isCarAlreadyRegistered(memberId)).thenReturn(true);
        when(carRegistrationService.isVerified(memberId)).thenReturn(true);
        
        ResponseEntity<Boolean> response = controller.isCarVerified(memberId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }
    
    @Test
    void testIsCarVerified_NotRegistered() {
        Long memberId = 1L;
        when(carRegistrationService.isCarAlreadyRegistered(memberId)).thenReturn(false);
        
        CustomException ex = assertThrows(CustomException.class, () ->
            controller.isCarVerified(memberId)
        );
        assertEquals(CarRegistrationErrorCode.CAR_NOT_FOUND, ex.getErrorCode());
    }
    
    // 8. GET /api/car-registration/member/{memberId}
    @Test
    void testGetCarByMemberId_Success() {
        Long memberId = 1L;
        CarRegistration car = mock(CarRegistration.class);
        when(carRegistrationService.getCarByMemberId(memberId)).thenReturn(Optional.of(car));
        
        // Static mock for SecurityUtil.getCurrentMemberId()
        try (MockedStatic<SecurityUtil> secMock = mockStatic(SecurityUtil.class)) {
            secMock.when(SecurityUtil::getCurrentMemberId).thenReturn(1L);
            // CarRegistrationResponseDTO 생성은 내부 로직에 의존하므로, 반환 객체가 null이 아님을 검증
            ResponseEntity<?> response = controller.getCarByMemberId(memberId);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
        }
    }
    
    @Test
    void testGetCarByMemberId_NotFound() {
        Long memberId = 1L;
        when(carRegistrationService.getCarByMemberId(memberId)).thenReturn(Optional.empty());
        
        CustomException ex = assertThrows(CustomException.class, () ->
            controller.getCarByMemberId(memberId)
        );
        assertEquals(CarRegistrationErrorCode.CAR_NOT_FOUND, ex.getErrorCode());
    }
    
    // 9. PUT /api/car-registration/update/{carId}
    @Test
    void testUpdateCar_Success() {
        Long carId = 10L;
        // Mock existing CarRegistration and CarUpdateRequestDTO
        CarRegistration car = mock(CarRegistration.class);
        Member member = mock(Member.class);
        when(member.getMemberId()).thenReturn(1L);
        when(car.getMember()).thenReturn(member);
        when(carRegistrationService.getCarByCarId(carId)).thenReturn(Optional.of(car));
        
        CarUpdateRequestDTO updateRequest = mock(CarUpdateRequestDTO.class);
        when(updateRequest.getCarNumber()).thenReturn(" XYZ 789 ");
        when(updateRequest.getCarModel()).thenReturn("ModelY");
        when(updateRequest.getMaxPassengers()).thenReturn(5);
        when(updateRequest.getColor()).thenReturn("Blue");
        when(updateRequest.getImageUrl()).thenReturn("http://s3.example.com/newcar.jpg");
        when(updateRequest.getImageName()).thenReturn("NewCarImage");
        when(updateRequest.getVerifiedFile()).thenReturn("http://s3.example.com/newagreement.pdf");
        when(updateRequest.getCarDescription()).thenReturn("Updated description");
        
        CarRegistration updatedCar = mock(CarRegistration.class);
        when(carRegistrationService.updateCar(car)).thenReturn(updatedCar);
        
        try (MockedStatic<SecurityUtil> secMock = mockStatic(SecurityUtil.class)) {
            secMock.when(SecurityUtil::getCurrentMemberId).thenReturn(1L);
            
            ResponseEntity<CarUpdateResponseDTO> response = (ResponseEntity<CarUpdateResponseDTO>) controller.updateCar(carId, updateRequest);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            
            // Verify that cleanCarNumber() was applied.
            ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
            verify(car).setCarNumber(captor.capture());
            assertEquals("XYZ789", captor.getValue());
        }
    }
    
    @Test
    void testUpdateCar_Unauthorized() {
        Long carId = 10L;
        CarUpdateRequestDTO updateRequest = mock(CarUpdateRequestDTO.class);
        
        try (MockedStatic<SecurityUtil> secMock = mockStatic(SecurityUtil.class)) {
            String errorMsg = "Unauthorized";
            secMock.when(SecurityUtil::getCurrentMemberId).thenThrow(new IllegalArgumentException(errorMsg));
            
            ResponseEntity<?> response = controller.updateCar(carId, updateRequest);
            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
            assertEquals(errorMsg, response.getBody());
        }
    }
    
    @Test
    void testUpdateCar_CarNotFound() {
        Long carId = 10L;
        CarUpdateRequestDTO updateRequest = mock(CarUpdateRequestDTO.class);
        when(carRegistrationService.getCarByCarId(carId)).thenReturn(Optional.empty());
        
        CustomException ex = assertThrows(CustomException.class, () ->
            controller.updateCar(carId, updateRequest)
        );
        assertEquals(CarRegistrationErrorCode.CAR_NOT_FOUND, ex.getErrorCode());
    }
    
    @Test
    void testUpdateCar_RoleNotModify() {
        Long carId = 10L;
        CarRegistration car = mock(CarRegistration.class);
        Member member = mock(Member.class);
        // Session member is 1L, but car's member is 2L
        when(member.getMemberId()).thenReturn(2L);
        when(car.getMember()).thenReturn(member);
        when(carRegistrationService.getCarByCarId(carId)).thenReturn(Optional.of(car));
        
        CarUpdateRequestDTO updateRequest = mock(CarUpdateRequestDTO.class);
        
        try (MockedStatic<SecurityUtil> secMock = mockStatic(SecurityUtil.class)) {
            secMock.when(SecurityUtil::getCurrentMemberId).thenReturn(1L);
            
            CustomException ex = assertThrows(CustomException.class, () ->
                controller.updateCar(carId, updateRequest)
            );
            assertEquals(CarRegistrationErrorCode.ROLE_NOT_MODIFY, ex.getErrorCode());
        }
    }
    
    // 10. DELETE /api/car-registration/delete/{carId}
    @Test
    void testDeleteCar_Success() {
        Long carId = 20L;
        CarRegistration car = mock(CarRegistration.class);
        Member member = mock(Member.class);
        when(member.getMemberId()).thenReturn(1L);
        when(car.getMember()).thenReturn(member);
        when(carRegistrationService.getCarByCarId(carId)).thenReturn(Optional.of(car));
        
        try (MockedStatic<SecurityUtil> secMock = mockStatic(SecurityUtil.class)) {
            secMock.when(SecurityUtil::getCurrentMemberId).thenReturn(1L);
            ResponseEntity<String> response = controller.deleteCar(carId);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("차량이 성공적으로 삭제되었습니다.", response.getBody());
            verify(carRegistrationService).deleteCar(carId);
        }
    }
    
    @Test
    void testDeleteCar_RoleNotDelete() {
        Long carId = 20L;
        CarRegistration car = mock(CarRegistration.class);
        Member member = mock(Member.class);
        // Session member is 1L, but car's member is 2L
        when(member.getMemberId()).thenReturn(2L);
        when(car.getMember()).thenReturn(member);
        when(carRegistrationService.getCarByCarId(carId)).thenReturn(Optional.of(car));
        
        try (MockedStatic<SecurityUtil> secMock = mockStatic(SecurityUtil.class)) {
            secMock.when(SecurityUtil::getCurrentMemberId).thenReturn(1L);
            CustomException ex = assertThrows(CustomException.class, () ->
                controller.deleteCar(carId)
            );
            assertEquals(CarRegistrationErrorCode.ROLE_NOT_DELETE, ex.getErrorCode());
        }
    }
}
