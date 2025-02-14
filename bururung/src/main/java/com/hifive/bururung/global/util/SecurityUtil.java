package com.hifive.bururung.global.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public class SecurityUtil {

    public static Long getCurrentMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || authentication.getPrincipal() == "anonymousUser") {
            throw new IllegalStateException("현재 인증된 사용자가 없습니다.");
        }

        Object principal = authentication.getPrincipal();
        
        if (principal instanceof User) {
            try {
                return Long.parseLong(((User) principal).getUsername());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("잘못된 회원 ID 형식입니다.");
            }
        }

        throw new IllegalStateException("잘못된 사용자 인증 정보입니다.");
    }
}