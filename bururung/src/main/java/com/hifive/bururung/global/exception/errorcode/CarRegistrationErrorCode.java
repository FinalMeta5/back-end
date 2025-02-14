package com.hifive.bururung.global.exception.errorcode;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CarRegistrationErrorCode implements ErrorCode {
    
    // ✅ 차량 관련 오류
    CAR_NOT_FOUND(HttpStatus.NOT_FOUND, "등록된 차량이 없습니다."),
    CAR_ALREADY_REGISTERED(HttpStatus.CONFLICT, "이미 등록된 차량이 있습니다."),
    
    // ✅ 권한 관련 오류
    ROLE_NOT_MODIFY(HttpStatus.FORBIDDEN, "차량 정보 수정 권한이 없습니다."),
    ROLE_NOT_DELETE(HttpStatus.FORBIDDEN, "차량 정보 삭제 권한이 없습니다."),
    
    // ✅ 잘못된 요청 관련 오류
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    INVALID_MAX_PASSENGERS(HttpStatus.BAD_REQUEST, "최대 탑승 인원은 1명 이상이어야 합니다."),
    DUPLICATE_CAR_NUMBER(HttpStatus.CONFLICT, "이미 존재하는 차량 번호입니다."),
    
    // ✅ 파일 관련 오류
    FILE_UPLOAD_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다."),
    FILE_IS_EMPTY(HttpStatus.BAD_REQUEST, "업로드된 파일이 없습니다.");

    private final HttpStatus status;
    private final String message;
}
