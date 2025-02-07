package com.hifive.bururung.global.exception.errorcode;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaxiShareErrorCode implements ErrorCode{
	
    TAXI_SHARE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 택시 공유 정보를 찾을 수 없습니다"),
    INVALID_PASSENGER_NUMBER(HttpStatus.BAD_REQUEST, "유효하지 않은 탑승 인원 수입니다"),
    INVALID_PICKUP_TIME(HttpStatus.BAD_REQUEST, "유효하지 않은 픽업 시간입니다"),
    INVALID_LOCATION(HttpStatus.BAD_REQUEST, "유효하지 않은 위치 정보입니다"),
    DUPLICATE_TAXI_SHARE(HttpStatus.CONFLICT, "이미 존재하는 택시 공유 정보입니다"),
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "택시 공유 정보에 대한 접근 권한이 없습니다");

    private final HttpStatus status;
    private final String message;

}
