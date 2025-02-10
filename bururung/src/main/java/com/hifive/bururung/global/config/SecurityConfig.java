package com.hifive.bururung.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.hifive.bururung.global.common.JwtAuthenticationFilter;
import com.hifive.bururung.global.common.TokenProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final TokenProvider tokenProvider;
	private final AuthenticationEntryPoint entryPoint;
	
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/api/member/login").permitAll()
                                .requestMatchers("/api/member/signup").permitAll()
                                .requestMatchers("/api/email/**").permitAll()
                                .requestMatchers("/api/member/find-email").permitAll()
                                .requestMatchers("/api/member/change-password").permitAll()
                                .requestMatchers("/api/car-registration/**").authenticated() // 차량 등록 API는 인증 필요
                                .requestMatchers("/error").permitAll()
                                .requestMatchers("/api/carshare/registration/available-list").permitAll()

                                .anyRequest().authenticated()
//                		.requestMatchers("/**").permitAll()
                )
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(formLogin -> formLogin.disable())
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(handler -> handler.authenticationEntryPoint(entryPoint));
        
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