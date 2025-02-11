package com.hifive.bururung.domain.carshare.participant.service;

import java.util.List;
import java.util.Map;

import com.hifive.bururung.domain.carshare.participant.dto.AvailableCarShareListResponse;
import com.hifive.bururung.domain.carshare.participant.dto.CarInformationResponse;
import com.hifive.bururung.domain.carshare.participant.dto.DriverInformationResponse;

public interface IServiceRegistrationService {
	List<AvailableCarShareListResponse> getAvailableCarShareList();
	DriverInformationResponse getDriverInformation(Long memberId);
	CarInformationResponse getCarInformation(Long memberId);
}
