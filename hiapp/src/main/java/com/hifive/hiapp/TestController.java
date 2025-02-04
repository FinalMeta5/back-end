package com.hifive.hiapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @GetMapping("/")
    public String testOpen() {
    	return "Welcome";
    }

    @GetMapping("/test-db")
    public String testDb() {
        return jdbcTemplate.queryForObject("SELECT 'DB 연결 성공' FROM DUAL", String.class);
    }
}