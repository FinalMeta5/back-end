package com.hifive.bururung.domain.member.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hifive.bururung.domain.member.dto.SignupRequest;
import com.hifive.bururung.domain.member.dto.TokenDTO;
import com.hifive.bururung.domain.member.entity.Member;
import com.hifive.bururung.domain.member.repository.MemberRepository;
import com.hifive.bururung.global.common.RedisUtil;
import com.hifive.bururung.global.common.TokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService implements IMemberService {
	
	private final MemberRepository memberRepository;
	private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisUtil redisUtil;
    private final PasswordEncoder passwordEncoder;
    
	@Value("${jwt.refresh-token-validity}")
	private Long refreshTokenValidity;
	private static final String REFRESH_TOKEN_PREFIX = "REFRESH_TOKEN:";
	
	@Override
	public TokenDTO login(String email, String password) {
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
		
		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		
		String accessToken = tokenProvider.createAccessToken(authentication);
		String refreshToken = tokenProvider.createRefreshToken(authentication);
		
		saveRefreshToken(authentication.getName(), refreshToken);
		
		return new TokenDTO(accessToken, refreshToken, authentication.getName());
	}
	
	@Override
	@Transactional
	public Long signup(SignupRequest signupRequest) {
		Member member = Member.builder()
				.birth(signupRequest.getBirth())
				.email(signupRequest.getEmail())
				.gender(signupRequest.getGender())
				.name(signupRequest.getName())
				.roleName("ROLE_USER")
				.password(passwordEncoder.encode(signupRequest.getPassword()))
				.phone(signupRequest.getPhone())
				.nickname(signupRequest.getNickname())
				.build();
		
		return memberRepository.save(member).getMemberId();
	}

	private void saveRefreshToken(String email, String refreshToken) {
		redisUtil.setData(REFRESH_TOKEN_PREFIX + email, refreshToken, Duration.ofSeconds(refreshTokenValidity));
	}
}
