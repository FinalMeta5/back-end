package com.hifive.bururung.global.exception.errorcode;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String getMessage();
    HttpStatus getStatus();
    String name();
}