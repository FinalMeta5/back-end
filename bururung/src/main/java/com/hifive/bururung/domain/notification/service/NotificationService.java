package com.hifive.bururung.domain.notification.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hifive.bururung.domain.notification.dto.NotificationDTO;
import com.hifive.bururung.domain.notification.entity.Notification;
import com.hifive.bururung.domain.notification.repository.INotificationMapper;
import com.hifive.bururung.domain.notification.repository.INotificationRepository;

@Service
public class NotificationService implements INotificationService {
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	@Autowired
	private INotificationRepository notificationRepository;
	@Autowired
	private INotificationMapper notificationMapper;

	@Override
	public List<Notification> getNotifications() {
		return notificationRepository.findAll();
	}

	@Override
	public List<NotificationDTO> getReadNotifications(Long memberId) {
		return notificationMapper.getReadNotifications(memberId);
	}

	@Override
	public List<NotificationDTO> getUnreadNotifications(Long memberId) {
		return notificationMapper.getUnreadNotifications(memberId);
	}

	@Override
	@Transactional
	public List<NotificationDTO> getNotificationsBySenderId(Long senderId) {
		List<Notification> notificationList = notificationRepository.findAllBySenderIdAndOpenStatusOrderByCreatedDateDesc(senderId, "Y");
		List<NotificationDTO> notificationDtoList = notificationList.stream().map(data -> {
			NotificationDTO newNoti = new NotificationDTO();
			newNoti.setNotificationId(data.getNotificationId());
			newNoti.setCreatedDate(data.getCreatedDate());
			newNoti.setCategory(data.getCategory());
			newNoti.setServiceCtg(data.getServiceCtg());
			newNoti.setContent(data.getContent());
			newNoti.setSenderId(data.getSenderId());
			newNoti.setRecipientId(data.getRecipientId());
			return newNoti;
		}).toList();
		return notificationDtoList;
	}

	@Override
	@Transactional
	public void sendNotification(Notification notification) {
		LocalDateTime now = LocalDateTime.now();
		notification.setCreatedDate(now);
		notificationMapper.insertNotification(notification);
		messagingTemplate.convertAndSend("/topic/notifications", this.getUnreadNotifications(notification.getRecipientId()));
	}

	@Override
	@Transactional
	public void readNotification(Long notificationId, Long memberId) {
		notificationMapper.updateNotificationToRead(notificationId);
		messagingTemplate.convertAndSend("/topic/notifications", this.getUnreadNotifications(memberId));
	}

	@Override
	public void deleteNotification(Long notificationId) {
		notificationMapper.updateNotificationToDelete(notificationId);
	}

}
