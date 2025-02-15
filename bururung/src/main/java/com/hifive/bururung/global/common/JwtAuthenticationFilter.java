package com.hifive.bururung.global.common;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	
	private static final String AUTH_HEADER = "Authorization";
	private final TokenProvider tokenProvider;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
			String jwt = resolveToken(request);
			
	        if(StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
	        	Authentication authentication = tokenProvider.getAuthentication(jwt);

	        	SecurityContextHolder.getContext().setAuthentication(authentication);
	        	log.info("SecurityContext에 '{}' 인증 정보 저장, uri: {}", authentication.getName(), request.getRequestURI());
	        } else {
	            log.info("유효한 JWT 토큰이 없음, uri: {}", request.getRequestURI());
	        }
        
        filterChain.doFilter(request, response);
	}
	
    public String resolveToken(HttpServletRequest request) {
    	String bearerToken = request.getHeader(AUTH_HEADER);
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        
        return null;
    }
}
