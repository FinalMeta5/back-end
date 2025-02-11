package com.hifive.bururung.domain.notification.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
	private Long notificationId;
	private LocalDateTime createdDate;
	private String category;
	private String serviceCtg;
    private String content;
    private Long senderId;
    private Long recipientId; 
}
