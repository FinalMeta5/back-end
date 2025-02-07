package com.hifive.bururung.global.exception.errorcode;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
@Getter
@AllArgsConstructor
public enum TaxiShareJoinErrorCode implements ErrorCode {
	JOIN_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 택시 공유 참여 정보를 찾을 수 없습니다"),
    ALREADY_JOINED(HttpStatus.CONFLICT, "이미 해당 택시 공유에 참여하셨습니다"),
    FULL_CAPACITY(HttpStatus.BAD_REQUEST, "택시 공유 정원이 이미 다 찼습니다"),
    INVALID_JOIN_TIME(HttpStatus.BAD_REQUEST, "유효하지 않은 참여 시간입니다"),
    UNAUTHORIZED_JOIN(HttpStatus.UNAUTHORIZED, "택시 공유 참여에 대한 권한이 없습니다"),
    CANNOT_JOIN_OWN_SHARE(HttpStatus.BAD_REQUEST, "자신이 만든 택시 공유에는 참여할 수 없습니다"),
    DUPLICATE_JOIN_ATTEMPT(HttpStatus.CONFLICT, "이미 참여한 택시 공유에 다시 참여할 수 없습니다"),
    JOIN_CANCELLATION_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "택시 공유 참여 취소가 불가능한 상태입니다");
	
	private final HttpStatus status;
    private final String message;
}
