package com.hifive.bururung.domain.member.service;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hifive.bururung.domain.member.entity.Member;
import com.hifive.bururung.domain.member.repository.MemberRepository;
import com.hifive.bururung.global.exception.CustomException;
import com.hifive.bururung.global.exception.errorcode.MemberErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final MemberRepository memberRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(MemberErrorCode.USER_NOT_FOUND));
        
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(member.getRoleName());

        return new User(member.getEmail(), member.getPassword(), authorities);
	}
}
