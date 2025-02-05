package com.hifive.bururung.global.common;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisUtil {
	
	private final RedisTemplate<String, String> redisTemplate;
	
    public void setData(String key, String value, Duration duration) {
        redisTemplate.opsForValue().set(key, value, duration);
    }
    
    public String getData(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteData(String key) {
        redisTemplate.delete(key);
    }
}
