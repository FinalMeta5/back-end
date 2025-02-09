package com.hifive.bururung.domain.taxi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxiShareJoinRequest {
	private Long taxiShareId;
	private Long memberId;
}
