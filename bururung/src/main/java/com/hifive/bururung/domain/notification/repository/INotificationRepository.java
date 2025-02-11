package com.hifive.bururung.domain.notification.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hifive.bururung.domain.notification.entity.Notification;

public interface INotificationRepository extends JpaRepository<Notification, Long> {

	List<Notification> findAllBySenderIdAndOpenStatusOrderByCreatedDateDesc(Long sender_id, String open_status);
	
}
