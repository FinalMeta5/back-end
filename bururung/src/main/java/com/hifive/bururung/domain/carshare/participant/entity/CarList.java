package com.hifive.bururung.domain.carshare.participant.entity;

import org.springframework.data.redis.core.RedisHash;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@RedisHash(value = "carList", timeToLive = 300)
public class CarList {
	private Long carId;
}
