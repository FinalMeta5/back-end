package com.hifive.bururung.domain.taxi.util;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;

import com.hifive.bururung.domain.member.entity.Member;
import com.hifive.bururung.domain.notification.entity.Notification;
import com.hifive.bururung.domain.taxi.dto.TaxiShareJoinRequest;
import com.hifive.bururung.domain.taxi.dto.TaxiShareResponse;

public class TaxiShareJoinAction {
	
	private static final Long operatorId = 42L; // 고정값

	// 참여 관련 알림 보내기
	public static Notification getTaxiShareJoinNotiInfo(TaxiShareResponse taxiShareResponse,
			TaxiShareJoinRequest taxiShareJoinRequest, Member participantInfo, int type) {

		LocalDateTime now = LocalDateTime.now();
		Notification notification = new Notification();
		notification.setServiceCtg("택시 공유 서비스");
		notification.setSenderId(operatorId);
		notification.setCreatedDate(now);

		String content = "";
		// 1 => 참여, to.참여자
		if (type == 1) {
			content = "호스트 " + taxiShareResponse.getMemberId() + "님의 택시 공유에 참여하셨습니다! 출발시간: "
					+ taxiShareResponse.getPickupTime() + " " + taxiShareResponse.getPickupTimeOnly() + ", 출발지: "
					+ taxiShareResponse.getPickupLocation() + ", 도착지: " + taxiShareResponse.getDestination()
					+ "\n오픈카톡방에 접속하셔서 의사소통해보세요!" + "\n오픈카톡 url: " + taxiShareResponse.getOpenchatLink() + ", 오픈카톡코드: "
					+ taxiShareResponse.getOpenchatCode();
			notification.setContent(content);
			notification.setCategory("신청완료");
			notification.setRecipientId(taxiShareJoinRequest.getMemberId());
		} else { // 2 => to.호스트에게 보냄
			content = taxiShareResponse.getMemberId() + "님이 개설하신 택시 공유에 참가자가 생겼습니다! 참가자닉네임: "
					+ participantInfo.getNickname();
			notification.setContent(content);
			notification.setCategory("신청알림");
			notification.setRecipientId(taxiShareResponse.getMemberId());
		}
		return notification;
	}

	// 삭제 관련 알림 보내기
	public static Notification getTaxiShareDeleteNotiInfo(TaxiShareResponse taxiShareResponse, 
			TaxiShareJoinRequest taxiShareJoinRequest, Member participantInfo) {
		
		LocalDateTime now = LocalDateTime.now();
		Notification notification = new Notification();
		notification.setServiceCtg("택시 공유 서비스");
		notification.setSenderId(operatorId);
		notification.setCreatedDate(now);
		String content = participantInfo.getNickname()+"고객님, 죄송합니다. 참여 신청하신 택시 공유가 호스트에 의해 삭제되었습니다. 다른 택시 공유를 신청해보세요!! \n"+
					"삭제된 신청정보\n"+
					" 출발지: "+taxiShareResponse.getPickupLocation()+
					", 도착지: "+taxiShareResponse.getDestination()+
					", 출발시간: "+taxiShareResponse.getPickupTime()+" "+taxiShareResponse.getPickupTimeOnly();
		
		notification.setContent(content);
		notification.setCategory("삭제알림");
		notification.setRecipientId(participantInfo.getMemberId());
		return notification;
	}

	// 크레딧 차감
	public void deductCredit(Long memberId, int amount) {
		// 크레딧 차감 로직
	}
}
