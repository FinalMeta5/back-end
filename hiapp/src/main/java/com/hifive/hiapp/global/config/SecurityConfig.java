package com.hifive.hiapp.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.hifive.hiapp.global.common.JwtAuthenticationFilter;
import com.hifive.hiapp.global.common.TokenProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final TokenProvider tokenProvider;
	
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/api/member/login").permitAll()
                                .requestMatchers("/api/member/signup").permitAll()
                                .requestMatchers("/error").permitAll()
                                .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(formLogin -> formLogin.disable())
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
    }
    
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
    	return new JwtAuthenticationFilter(tokenProvider);
    }
    
}