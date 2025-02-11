package com.hifive.bururung.domain.notification.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hifive.bururung.domain.notification.dto.NotificationDTO;
import com.hifive.bururung.domain.notification.dto.NotificationListResponse;
import com.hifive.bururung.domain.notification.entity.Notification;
import com.hifive.bururung.domain.notification.service.INotificationService;
import com.hifive.bururung.global.util.SecurityUtil;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private INotificationService notificationService;
    
    @GetMapping
    public List<Notification> getNotifications() {
    	return notificationService.getNotifications();
    }
    
    @GetMapping("/read")
    public ResponseEntity<NotificationListResponse<List<NotificationDTO>>> getReadNotifications() {
    	Long sessionMemberId;
	    try {
	        sessionMemberId = SecurityUtil.getCurrentMemberId();
	    } catch (IllegalArgumentException | IllegalStateException e) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new NotificationListResponse<>(null, e.getMessage()));
	    }
	    return ResponseEntity.ok(new NotificationListResponse<>(notificationService.getReadNotifications(sessionMemberId), null));
    }
    
    @GetMapping("/unread")
    public ResponseEntity<NotificationListResponse<List<NotificationDTO>>> getUnreadNotifications() {
    	Long sessionMemberId;
    	try {
    		sessionMemberId = SecurityUtil.getCurrentMemberId();
    	} catch (IllegalArgumentException | IllegalStateException e) {
    		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new NotificationListResponse<>(null, e.getMessage()));
    	}
    	return ResponseEntity.ok(new NotificationListResponse<>(notificationService.getUnreadNotifications(sessionMemberId), null));
    }
    
    @GetMapping("/bysender")
    public ResponseEntity<NotificationListResponse<List<NotificationDTO>>> getNotificationBySenderId() {
    	Long sessionMemberId;
    	try {
    		sessionMemberId = SecurityUtil.getCurrentMemberId();
    	} catch (IllegalArgumentException | IllegalStateException e) {
    		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new NotificationListResponse<>(null, e.getMessage()));
    	}
    	return ResponseEntity.ok(new NotificationListResponse<>(notificationService.getNotificationsBySenderId(sessionMemberId), null));
    }

    @PutMapping("/{notificationId}")
    public ResponseEntity<NotificationListResponse<List<NotificationDTO>>> readNotification(@PathVariable("notificationId") Long notificationId) {
    	Long sessionMemberId;
	    try {
	        sessionMemberId = SecurityUtil.getCurrentMemberId();
	        notificationService.readNotification(notificationId, sessionMemberId);
	    } catch (IllegalArgumentException | IllegalStateException e) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new NotificationListResponse<>(null, e.getMessage()));
	    }
	    return ResponseEntity.ok(new NotificationListResponse<>(notificationService.getReadNotifications(sessionMemberId), null));
    }
    
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<String> deleteNotification(@PathVariable("notificationId") Long notificationId) {
    	try {
    		notificationService.deleteNotification(notificationId);
    		return ResponseEntity.ok("ok");			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
    }

//	@PostMapping("/{content}")
//	public ResponseEntity<String> sendNotification(@PathVariable("content") String content) {
//		LocalDateTime now = LocalDateTime.now();
//		System.out.println("localDateTime: " + now);
//		Notification notification = new Notification(content);
//		notification.setCategory("카테고리" + (int)((Math.random()*10)+ 1));
//		notification.setServiceCtg("서비스유형" + (int)((Math.random()*10)+ 1));
//		notification.setSenderId(41l);
//		notification.setRecipientId(41l);
//		notification.setCreatedDate(now);
//		notificationService.sendNotification(notification);
//	    return ResponseEntity.ok("Notification sent");
//	}
}
