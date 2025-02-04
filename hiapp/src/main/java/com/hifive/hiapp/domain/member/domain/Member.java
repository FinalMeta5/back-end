package com.hifive.hiapp.domain.member.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
	
	@Id
	@SequenceGenerator(
			name = "MEMBER_SEQ_GEN",
			sequenceName = "MEMBER_SEQ",
			allocationSize = 1
	)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_SEQ_GEN")
	private Long memberId;
	
	private String email;
	
	private String phone;
	
	private String password;
	
	private String name;
	
	private String birth;
	
	private Character gender;
	
	@Enumerated(EnumType.STRING)
	private MemberState state;
		
	private LocalDateTime joinDate;
	
	private LocalDateTime withdrawalDate;
	
	private String nickname;
	
	private String imageUrl;
	
	private String imageName;
	
	private Integer creditCount;
	
	private String roleName;

	@Builder
	public Member(String email, String phone, String password, String name, String birth, Character gender, String roleName, String nickname) {
		this.email = email;
		this.phone = phone;
		this.password = password;
		this.name = name;
		this.birth = birth;
		this.gender = gender;
		this.roleName = roleName;
		this.state = MemberState.ACTIVE;
		this.joinDate = LocalDateTime.now();
		this.withdrawalDate = LocalDateTime.now();
		this.creditCount = 0;
		this.nickname = nickname;
	}
}
