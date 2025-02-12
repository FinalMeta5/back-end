package com.hifive.bururung.domain.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminMemberDTO {
	private Long memberId;
	private String birth;
	private String email;
	private String gender;
	private String joinDate;
	private String name;
	private String phone;
	private String roleName;
	private String withdrawalDate;
	private int creditCount;
	private String imageName;
	private String imageUrl;
	private String nickname;
}
