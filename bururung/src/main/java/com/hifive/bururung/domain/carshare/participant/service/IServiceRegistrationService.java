package com.hifive.bururung.domain.carshare.participant.service;

import java.util.List;

import com.hifive.bururung.domain.carshare.participant.dto.AvailableCarShareListResponse;

public interface IServiceRegistrationService {
	List<AvailableCarShareListResponse> getAvailableCarShareList();
}
