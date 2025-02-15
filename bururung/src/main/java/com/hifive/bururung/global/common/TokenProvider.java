package com.hifive.bururung.global.common;

import java.util.Base64;
import java.util.Collections;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.hifive.bururung.global.exception.CustomException;
import com.hifive.bururung.global.exception.errorcode.MemberErrorCode;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TokenProvider implements InitializingBean{
	
	@Value("${jwt.secret}")
	private String secretKey;
	
	@Value("${jwt.access-token-validity}")
	private Long accessTokenValidity;
	
	@Value("${jwt.refresh-token-validity}")
	private Long refreshTokenValidity;
	
	private SecretKey key;
	
    @Override
    public void afterPropertiesSet() {
     	byte[] keyBytes = Base64.getDecoder().decode(secretKey);
		this.key = Keys.hmacShaKeyFor(keyBytes); 
    }
	
    public String createAccessToken(Authentication authentication) {
        // 🔥 단일 ROLE 값 가져오기
        String roleName = authentication.getAuthorities().stream()
                .findFirst() // ✅ 가장 첫 번째 ROLE만 가져오기
                .map(GrantedAuthority::getAuthority)
                .orElse("USER"); // 기본값 USER

        long now = (new Date()).getTime();

        return Jwts.builder()
                .subject(authentication.getName())
                .claim("ROLE_NAME", roleName) // ✅ 단일 ROLE 저장
                .signWith(key)
                .expiration(new Date(now + accessTokenValidity * 1000))
                .compact();
    }
	
	public String createRefreshToken(Authentication authentication) {
        String roleName = authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("USER");
        long now = (new Date()).getTime();
        
        return Jwts.builder()
        		.subject(authentication.getName())
        		.claim("ROLE_NAME", roleName)
                .signWith(key)
                .expiration(new Date(now + refreshTokenValidity * 1000))
                .compact();
	}
	
	public boolean validateToken(String token) {
	    try {
	        Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
	        return true;
	    } catch (ExpiredJwtException e) {
	        throw new CredentialsExpiredException("토큰 만료", e);
	    } catch(Exception e) {
	        throw new BadCredentialsException("올바르지 않은 토큰", e);
	    }
	}
	
    public Authentication getAuthentication(String token) {
        Claims claims = getClaim(token);

        // 🔥 단일 ROLE 값 가져오기
        String roleName = claims.get("ROLE_NAME", String.class); // ✅ 단일 값 그대로 사용

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + roleName); // ✅ ROLE_NAME 사용
        User user = new User(claims.getSubject(), "", Collections.singletonList(authority));

        return new UsernamePasswordAuthenticationToken(user, token, Collections.singletonList(authority));
    }
	
    private Claims getClaim(String token) {
        try {
            return Jwts.parser()
            		.verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
	
	
