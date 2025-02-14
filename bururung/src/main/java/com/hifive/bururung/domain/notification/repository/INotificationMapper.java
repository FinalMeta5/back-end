package com.hifive.bururung.domain.notification.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.hifive.bururung.domain.notification.dto.NotificationDTO;
import com.hifive.bururung.domain.notification.entity.Notification;

@Repository
@Mapper
public interface INotificationMapper {

	List<NotificationDTO> getReadNotifications(Long memberId);

	List<NotificationDTO> getUnreadNotifications(Long memberId);

	void insertNotification(Notification notification);
	
	void updateNotificationToRead(Long notificationId);

	void updateNotificationToDelete(Long notificationId);

}
