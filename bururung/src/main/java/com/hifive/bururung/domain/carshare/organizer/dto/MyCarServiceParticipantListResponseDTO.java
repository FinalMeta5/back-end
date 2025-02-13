package com.hifive.bururung.domain.carshare.organizer.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Data
public class MyCarServiceParticipantListResponseDTO {
	private Long carShareJoinId; // 참여일
	private Long memberId; // 참가자 아이디
	private String state; // 참가자 탑승 상태
	private Timestamp joinDate; // 참가 신청 날짜
	private double rate; // 참가자의 별점
	private Timestamp  createDate; // 리뷰 작성일
	private String comment; // 참가자 후기
	private String nickName; // 참가자 닉네임
	private String imageUrl; // 참가자 이미지
}
