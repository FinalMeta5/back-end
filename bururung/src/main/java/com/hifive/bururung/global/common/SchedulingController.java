package com.hifive.bururung.global.common;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hifive.bururung.domain.member.entity.Member;
import com.hifive.bururung.domain.member.service.IMemberService;
import com.hifive.bururung.domain.notification.entity.Notification;
import com.hifive.bururung.domain.notification.service.INotificationService;
import com.hifive.bururung.domain.taxi.service.ITaxiShareJoinService;

@RestController
@RequestMapping("/api/scheduling")
public class SchedulingController {
	@Autowired
	ITaxiShareJoinService taxiShareJoinService;
	@Autowired
	IMemberService memberService;
	@Autowired
	INotificationService notificationServie;
	
	private static final Long OPERATORID = 42L; // 고정값
	
	@Scheduled(cron = "0 31 18 * * ?")
	public ResponseEntity<Void> sendCarShareReviewNotification(){
		List<HashMap<String, Object>> list = taxiShareJoinService.getCarShareCountByMemberIdAndSysdate();
		//각 회원에게 알림보내기
		Notification notification = new Notification();
		LocalDateTime now = LocalDateTime.now();
		notification.setServiceCtg("차량 공유 서비스");
		notification.setSenderId(OPERATORID);
		notification.setCreatedDate(now);
		notification.setCategory("시스템알림");
		for(int i=0; i<list.size();i++) {
			Long memberId = Long.valueOf(String.valueOf(list.get(i).get("MEMBER_ID")));
			Member memberInfo = memberService.findByMemberId(memberId); 
			String content = "안녕하세요 "+memberInfo.getNickname()+"님! \n"+
							"오늘 참여하신 차량공유가 "+list.get(i).get("CNT")+"건 있으세요! \n "
									+ "후기를 남겨주세요!!\n"
									+ "링크: "; //나중에 링크넣기
			notification.setContent(content);
			notification.setRecipientId(memberId);
			notificationServie.sendNotification(notification);
		}
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
