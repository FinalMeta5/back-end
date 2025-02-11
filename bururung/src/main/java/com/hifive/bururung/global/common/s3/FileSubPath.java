package com.hifive.bururung.global.common.s3;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileSubPath {
	MEMBER("member/"),
	CAR("car/"),
	VERIFIED("verified/");
	
	private final String path;
}
