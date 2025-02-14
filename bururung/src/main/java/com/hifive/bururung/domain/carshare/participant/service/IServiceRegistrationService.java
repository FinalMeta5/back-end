package com.hifive.bururung.domain.carshare.participant.service;

import java.util.List;
import java.util.Map;

import com.hifive.bururung.domain.carshare.participant.dto.AllCarListResponse;
import com.hifive.bururung.domain.carshare.participant.dto.AvailableCarShareListResponse;
import com.hifive.bururung.domain.carshare.participant.dto.CarInformationResponse;
import com.hifive.bururung.domain.carshare.participant.dto.DriverInformationResponse;
import com.hifive.bururung.domain.carshare.participant.dto.DrivingInformationResponse;
import com.hifive.bururung.domain.carshare.participant.dto.PastParticipationListResponse;

public interface IServiceRegistrationService {
	List<AvailableCarShareListResponse> getAvailableCarShareList();
	DriverInformationResponse getDriverInformation(Long memberId);
	CarInformationResponse getCarInformation(Long memberId);
	DrivingInformationResponse getDrivingInformation(Long memberId, Long carShareRegiId);
	boolean insertRegistration(Long carShareRegiId, Long userId);
	Double findRating(Long memberId);
	int findLeftoverCredit(Long userId);
	void insertCreditByCar(Long userId);
	List<AllCarListResponse> findAllShareCarList();
	List<PastParticipationListResponse> findPastParticipationList(Long userId);
	List<PastParticipationListResponse> findTodayParticipationList(Long userId);
	int updateStateOK(Long carShareJoinId);
	int updateStateNO(Long carShareJoinId);
	List<AllCarListResponse> findByCategoryShareCarList(String category);

}
