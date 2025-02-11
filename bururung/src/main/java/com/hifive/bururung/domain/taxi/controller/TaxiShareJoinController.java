package com.hifive.bururung.domain.taxi.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hifive.bururung.domain.member.entity.Member;
import com.hifive.bururung.domain.member.service.IMemberService;
import com.hifive.bururung.domain.notification.entity.Notification;
import com.hifive.bururung.domain.notification.service.INotificationService;
import com.hifive.bururung.domain.taxi.dto.TaxiShareJoinRequest;
import com.hifive.bururung.domain.taxi.dto.TaxiShareResponse;
import com.hifive.bururung.domain.taxi.service.ITaxiShareJoinService;
import com.hifive.bururung.domain.taxi.service.ITaxiShareService;
import com.hifive.bururung.domain.taxi.util.TaxiShareJoinAction;
import com.hifive.bururung.global.exception.CustomException;
import com.hifive.bururung.global.exception.errorcode.TaxiShareJoinErrorCode;

@RestController
@RequestMapping("/api/taxi/join")
public class TaxiShareJoinController {
	@Autowired
	ITaxiShareJoinService taxiShareJoinService;
	@Autowired
	ITaxiShareService taxiShareService;
	@Autowired
	INotificationService notificationService;
	@Autowired
	IMemberService memberService;

	// 방 참여자 수
	@GetMapping("/count/{taxiShareId}")
	public ResponseEntity<Integer> getJoinCountByTaxiShareId(@PathVariable("taxiShareId") Long taxiShareId) {
		return ResponseEntity.ok(taxiShareJoinService.getJoinCountByTaxiShareId(taxiShareId));
	}

	// 참여
	@PostMapping("/insert")
	public ResponseEntity<Void> insertTaxiShareJoin(@RequestBody TaxiShareJoinRequest taxiShareJoinRequest) {
		// 이미 참여한 택시인지 확인
		int duplCnt = taxiShareJoinService.getDuplCntByTaxiShareIdAndMemberId(taxiShareJoinRequest);
		// 내가 호스트인지 확인
		int isHost = taxiShareService.getCountTaxsiShareByIdAndMemberId(taxiShareJoinRequest);
		// 해당 택시 정보
		TaxiShareResponse taxiSahreResponse = taxiShareService.getTaxiShareById(taxiShareJoinRequest.getTaxiShareId());
		// 참여자 멤버 정보
		Member participantInfo = memberService.findByMemberId(taxiShareJoinRequest.getMemberId());
		if (duplCnt < 1) { // 참여한 적이 없으면
			if (isHost < 1) { // 호스트가 아니면
				// 택시 조인 insert(참여)
				taxiShareJoinService.insertTaxiShareJoin(taxiShareJoinRequest);

				// 알림 보내기
				// 참여자에게 보내기 => type=1
				Notification notification2Participant = TaxiShareJoinAction.getTaxiShareJoinNotiInfo(taxiSahreResponse,
						taxiShareJoinRequest, participantInfo, 1);
				System.out.println(notification2Participant.toString());
				notificationService.sendNotification(notification2Participant);
				// 호스트에게 보내기=> type=2
				Notification notification2Host = TaxiShareJoinAction.getTaxiShareJoinNotiInfo(taxiSahreResponse,
						taxiShareJoinRequest, participantInfo, 2);
				System.out.println(notification2Host.toString());
				notificationService.sendNotification(notification2Host);
				// 크레딧 차감
				// 나중에
				return ResponseEntity.status(HttpStatus.CREATED).build();
			} else {
				System.out.println("본인이 호스트인 방엔 참여할 수 없음!!");
				throw new CustomException(TaxiShareJoinErrorCode.CANNOT_JOIN_OWN_SHARE);
			}
		} else {
			System.out.println("한사람이 똑같은 방에 참여할 수 없음!!!");
			throw new CustomException(TaxiShareJoinErrorCode.DUPLICATE_JOIN_ATTEMPT);
		}
	}
}
