package com.hifive.bururung.domain.taxi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LatLng {
	private double lat;
	private double lng;
}
