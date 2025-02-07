package com.hifive.bururung.domain.taxi.entity;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
public class TaxiShareJoin {
	private Long tsjId;
	private int taxiShareId;
	private Long memberId;
	private Date joinTime; 
}
