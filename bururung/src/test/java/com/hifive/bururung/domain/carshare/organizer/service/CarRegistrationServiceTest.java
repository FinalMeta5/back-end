package com.hifive.bururung.domain.carshare.organizer.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.hifive.bururung.domain.carshare.organizer.entity.CarRegistration;
import com.hifive.bururung.domain.carshare.organizer.repository.CarRegistrationRepository;
import com.hifive.bururung.global.common.s3.S3Uploader;
import com.hifive.bururung.global.common.s3.UploadFileDTO;
import com.hifive.bururung.global.exception.CustomException;

@ExtendWith(MockitoExtension.class)
public class CarRegistrationServiceTest {

    @InjectMocks
    private CarRegistrationService service;

    @Mock
    private CarRegistrationRepository carRegistrationRepository;

    @Mock
    private S3Uploader s3Uploader;

    // ────────── isCarAlreadyRegistered ──────────

    @Test
    void testIsCarAlreadyRegistered_True() {
        Long memberId = 1L;
        when(carRegistrationRepository.existsByMember_MemberId(memberId)).thenReturn(true);
        assertTrue(service.isCarAlreadyRegistered(memberId));
        verify(carRegistrationRepository).existsByMember_MemberId(memberId);
    }

    @Test
    void testIsCarAlreadyRegistered_False() {
        Long memberId = 1L;
        when(carRegistrationRepository.existsByMember_MemberId(memberId)).thenReturn(false);
        assertFalse(service.isCarAlreadyRegistered(memberId));
        verify(carRegistrationRepository).existsByMember_MemberId(memberId);
    }

    // ────────── isVerified ──────────

    @Test
    void testIsVerified_True() {
        Long memberId = 1L;
        when(carRegistrationRepository.findVerifiedByMemberId(memberId)).thenReturn("Y");
        assertTrue(service.isVerified(memberId));
        verify(carRegistrationRepository).findVerifiedByMemberId(memberId);
    }

    @Test
    void testIsVerified_False() {
        Long memberId = 1L;
        when(carRegistrationRepository.findVerifiedByMemberId(memberId)).thenReturn("N");
        assertFalse(service.isVerified(memberId));
        verify(carRegistrationRepository).findVerifiedByMemberId(memberId);
    }

    @Test
    void testIsVerified_Null() {
        Long memberId = 1L;
        when(carRegistrationRepository.findVerifiedByMemberId(memberId)).thenReturn(null);
        assertFalse(service.isVerified(memberId));
        verify(carRegistrationRepository).findVerifiedByMemberId(memberId);
    }

    // ────────── registerCar ──────────

    @Test
    void testRegisterCar() {
        CarRegistration car = new CarRegistration();
        when(carRegistrationRepository.save(car)).thenReturn(car);
        CarRegistration result = service.registerCar(car);
        assertSame(car, result);
        verify(carRegistrationRepository).save(car);
    }

    // ────────── getAllCars ──────────

    @Test
    void testGetAllCars() {
        List<CarRegistration> list = Arrays.asList(new CarRegistration(), new CarRegistration());
        when(carRegistrationRepository.findAll()).thenReturn(list);
        List<CarRegistration> result = service.getAllCars();
        assertEquals(list, result);
        verify(carRegistrationRepository).findAll();
    }

    // ────────── getCarByCarId ──────────

    @Test
    void testGetCarByCarId_Present() {
        Long carId = 10L;
        CarRegistration car = new CarRegistration();
        when(carRegistrationRepository.findById(carId)).thenReturn(Optional.of(car));
        Optional<CarRegistration> result = service.getCarByCarId(carId);
        assertTrue(result.isPresent());
        assertSame(car, result.get());
        verify(carRegistrationRepository).findById(carId);
    }

    @Test
    void testGetCarByCarId_Empty() {
        Long carId = 10L;
        when(carRegistrationRepository.findById(carId)).thenReturn(Optional.empty());
        Optional<CarRegistration> result = service.getCarByCarId(carId);
        assertFalse(result.isPresent());
        verify(carRegistrationRepository).findById(carId);
    }

    // ────────── getCarByCarNumber ──────────

    @Test
    void testGetCarByCarNumber_Present() {
        String carNumber = "ABC123";
        CarRegistration car = new CarRegistration();
        when(carRegistrationRepository.findByCarNumber(carNumber)).thenReturn(Optional.of(car));
        Optional<CarRegistration> result = service.getCarByCarNumber(carNumber);
        assertTrue(result.isPresent());
        assertSame(car, result.get());
        verify(carRegistrationRepository).findByCarNumber(carNumber);
    }

    @Test
    void testGetCarByCarNumber_Empty() {
        String carNumber = "ABC123";
        when(carRegistrationRepository.findByCarNumber(carNumber)).thenReturn(Optional.empty());
        Optional<CarRegistration> result = service.getCarByCarNumber(carNumber);
        assertFalse(result.isPresent());
        verify(carRegistrationRepository).findByCarNumber(carNumber);
    }

    // ────────── getCarByMemberId ──────────

    @Test
    void testGetCarByMemberId_Present() {
        Long memberId = 1L;
        CarRegistration car = new CarRegistration();
        when(carRegistrationRepository.findByMember_MemberId(memberId)).thenReturn(Optional.of(car));
        Optional<CarRegistration> result = service.getCarByMemberId(memberId);
        assertTrue(result.isPresent());
        assertSame(car, result.get());
        verify(carRegistrationRepository).findByMember_MemberId(memberId);
    }

    @Test
    void testGetCarByMemberId_Empty() {
        Long memberId = 1L;
        when(carRegistrationRepository.findByMember_MemberId(memberId)).thenReturn(Optional.empty());
        Optional<CarRegistration> result = service.getCarByMemberId(memberId);
        assertFalse(result.isPresent());
        verify(carRegistrationRepository).findByMember_MemberId(memberId);
    }

    // ────────── updateCar ──────────

    @Test
    void testUpdateCar() {
        CarRegistration car = new CarRegistration();
        when(carRegistrationRepository.save(car)).thenReturn(car);
        CarRegistration result = service.updateCar(car);
        assertSame(car, result);
        verify(carRegistrationRepository).save(car);
    }

    // ────────── deleteCar ──────────

    @Test
    void testDeleteCar_Success() {
        Long carId = 5L;
        when(carRegistrationRepository.existsById(carId)).thenReturn(true);
        service.deleteCar(carId);
        verify(carRegistrationRepository).existsById(carId);
        verify(carRegistrationRepository).deleteById(carId);
    }

    @Test
    void testDeleteCar_Failure() {
        Long carId = 5L;
        when(carRegistrationRepository.existsById(carId)).thenReturn(false);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.deleteCar(carId));
        assertEquals("해당 차량이 존재하지 않습니다.", exception.getMessage());
        verify(carRegistrationRepository).existsById(carId);
        verify(carRegistrationRepository, never()).deleteById(carId);
    }

    // ────────── isCarNumberExists ──────────

    @Test
    void testIsCarNumberExists_True() {
        String carNumber = "ABC123";
        when(carRegistrationRepository.countByCarNumber(carNumber)).thenReturn(2L);
        assertTrue(service.isCarNumberExists(carNumber));
        verify(carRegistrationRepository).countByCarNumber(carNumber);
    }

    @Test
    void testIsCarNumberExists_False_Zero() {
        String carNumber = "ABC123";
        when(carRegistrationRepository.countByCarNumber(carNumber)).thenReturn(0L);
        assertFalse(service.isCarNumberExists(carNumber));
        verify(carRegistrationRepository).countByCarNumber(carNumber);
    }

    @Test
    void testIsCarNumberExists_False_Null() {
        String carNumber = "ABC123";
        when(carRegistrationRepository.countByCarNumber(carNumber)).thenReturn(null);
        assertFalse(service.isCarNumberExists(carNumber));
        verify(carRegistrationRepository).countByCarNumber(carNumber);
    }

    // ────────── uploadCarImage ──────────

    @Test
    void testUploadCarImage_CarNotFound() {
        Long memberId = 1L;
        String subpath = "cars/";
        MultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "dummy".getBytes());
        when(carRegistrationRepository.findByMember_MemberId(memberId)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () ->
            service.uploadCarImage(file, subpath, memberId)
        );
        // 추가 검증: 예외 코드 등 (필요시)
        verify(carRegistrationRepository).findByMember_MemberId(memberId);
        verifyNoInteractions(s3Uploader);
    }

    @Test
    void testUploadCarImage_WithExistingImage() throws IOException {
        Long memberId = 1L;
        String subpath = "cars/";
        MultipartFile file = new MockMultipartFile("file", "new.jpg", "image/jpeg", "new image".getBytes());

        CarRegistration car = new CarRegistration();
        car.setImageUrl("existingUrl");
        car.setImageName("oldImage.jpg");

        when(carRegistrationRepository.findByMember_MemberId(memberId)).thenReturn(Optional.of(car));

        UploadFileDTO uploadResult = new UploadFileDTO();
        uploadResult.setStoreFileName("newImage.jpg");
        uploadResult.setStoreFullUrl("http://s3.example.com/newImage.jpg");

        when(s3Uploader.uploadFile(file, subpath)).thenReturn(uploadResult);

        String resultUrl = service.uploadCarImage(file, subpath, memberId);

        // 기존 이미지가 존재하므로 삭제 호출
        verify(s3Uploader).deleteFile(subpath + "oldImage.jpg");
        verify(s3Uploader).uploadFile(file, subpath);
        assertEquals("http://s3.example.com/newImage.jpg", resultUrl);
        assertEquals("newImage.jpg", car.getImageName());
        assertEquals("http://s3.example.com/newImage.jpg", car.getImageUrl());
    }

    @Test
    void testUploadCarImage_NoExistingImage() throws IOException {
        Long memberId = 1L;
        String subpath = "cars/";
        MultipartFile file = new MockMultipartFile("file", "new.jpg", "image/jpeg", "new image".getBytes());

        CarRegistration car = new CarRegistration();
        car.setImageUrl(""); // 빈 문자열로 기존 이미지 없음
        car.setImageName(null);

        when(carRegistrationRepository.findByMember_MemberId(memberId)).thenReturn(Optional.of(car));

        UploadFileDTO uploadResult = new UploadFileDTO();
        uploadResult.setStoreFileName("newImage.jpg");
        uploadResult.setStoreFullUrl("http://s3.example.com/newImage.jpg");

        when(s3Uploader.uploadFile(file, subpath)).thenReturn(uploadResult);

        String resultUrl = service.uploadCarImage(file, subpath, memberId);

        verify(s3Uploader, never()).deleteFile(anyString());
        verify(s3Uploader).uploadFile(file, subpath);
        assertEquals("http://s3.example.com/newImage.jpg", resultUrl);
        assertEquals("newImage.jpg", car.getImageName());
        assertEquals("http://s3.example.com/newImage.jpg", car.getImageUrl());
    }

    // ────────── uploadVerifiedFile ──────────

    @Test
    void testUploadVerifiedFile_CarNotFound() {
        Long memberId = 1L;
        String subpath = "verified/";
        MultipartFile file = new MockMultipartFile("file", "verified.pdf", "application/pdf", "dummy".getBytes());
        when(carRegistrationRepository.findByMember_MemberId(memberId)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () ->
            service.uploadVerifiedFile(file, subpath, memberId)
        );
        verify(carRegistrationRepository).findByMember_MemberId(memberId);
        verifyNoInteractions(s3Uploader);
    }

    @Test
    void testUploadVerifiedFile_Success() throws IOException {
        Long memberId = 1L;
        String subpath = "verified/";
        MultipartFile file = new MockMultipartFile("file", "verified.pdf", "application/pdf", "dummy".getBytes());

        CarRegistration car = new CarRegistration();
        when(carRegistrationRepository.findByMember_MemberId(memberId)).thenReturn(Optional.of(car));

        UploadFileDTO uploadResult = new UploadFileDTO();
        uploadResult.setStoreFileName("verifiedFile.pdf");
        uploadResult.setStoreFullUrl("http://s3.example.com/verifiedFile.pdf");

        when(s3Uploader.uploadFile(file, subpath)).thenReturn(uploadResult);

        String resultUrl = service.uploadVerifiedFile(file, subpath, memberId);

        verify(s3Uploader).uploadFile(file, subpath);
        assertEquals("http://s3.example.com/verifiedFile.pdf", resultUrl);
        // 최종적으로 verifiedFile에 저장된 값은 full URL
        assertEquals("http://s3.example.com/verifiedFile.pdf", car.getVerifiedFile());
    }
}

