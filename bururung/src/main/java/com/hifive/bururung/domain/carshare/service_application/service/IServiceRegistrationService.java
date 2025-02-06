package com.hifive.bururung.domain.carshare.service_application.service;

import java.util.List;

import com.hifive.bururung.domain.carshare.service_application.dto.AvailableCarShareListResponse;

public interface IServiceRegistrationService {
	List<AvailableCarShareListResponse> getAvailableCarShareList();
}
