package com.hifive.bururung.global.common;

import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

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
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        
        long now = (new Date()).getTime();
        
        return Jwts.builder()
        		.subject(authentication.getName())
                .claim("role", authorities)
                .signWith(key)
                .expiration(new Date(now + accessTokenValidity * 1000))
                .compact();
	}
	
	public String createRefreshToken(Authentication authentication) {
        long now = (new Date()).getTime();
        
        return Jwts.builder()
                .signWith(key)
                .expiration(new Date(now + refreshTokenValidity * 1000))
                .compact();
	}
	
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
            
        }catch(ExpiredJwtException e){
        	log.info("만료된 JWT");
        }catch(Exception e){
        	log.info("잘못된 JWT");
        }
        
        return false;
    }
	
    public Authentication getAuthentication(String token) {
        Claims claims = getClaim(token);

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("role").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User user = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(user, token, authorities);
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
	
	
