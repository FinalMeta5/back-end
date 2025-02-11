package com.hifive.bururung.domain.notification.service;

import java.util.List;

import com.hifive.bururung.domain.notification.dto.NotificationDTO;
import com.hifive.bururung.domain.notification.entity.Notification;

public interface INotificationService {

	List<Notification> getNotifications();
	
	List<NotificationDTO> getReadNotifications(Long memberId);
	
	List<NotificationDTO> getUnreadNotifications(Long memberId);
	
	List<NotificationDTO> getNotificationsBySenderId(Long senderId);
	
	void sendNotification(Notification notification);
	
	void readNotification(Long notificationId, Long memberId);
	
	void deleteNotification(Long notificationId);
	
}
