package com.hifive.bururung.domain.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminCarShareResponse<T> {
	private boolean success;
	private T data;
	private String message;
}
