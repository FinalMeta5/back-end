package com.hifive.bururung.global.common.s3;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileSubPath {
	MEMBER("member/"),
	CAR("car/");
	
	private final String path;
}
