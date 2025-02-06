package com.hifive.bururung;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @GetMapping("/")
    public String testOpen(@AuthenticationPrincipal User user) {
    	return user.getUsername();
    }

    @GetMapping("/test-db")
    public String testDb() {
        return jdbcTemplate.queryForObject("SELECT 'DB 연결 성공' FROM DUAL", String.class);
    }
}