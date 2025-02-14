package com.hifive.bururung.domain.notification.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.hifive.bururung.domain.notification.dto.NotificationDTO;
import com.hifive.bururung.domain.notification.entity.Notification;
import com.hifive.bururung.domain.notification.repository.INotificationMapper;
import com.hifive.bururung.domain.notification.repository.INotificationRepository;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @InjectMocks
    private NotificationService notificationService;
    
    @Mock
    private SimpMessagingTemplate messagingTemplate;
    
    @Mock
    private INotificationRepository notificationRepository;
    
    @Mock
    private INotificationMapper notificationMapper;
    
    // 1. getNotifications()
    @Test
    void testGetNotifications() {
        List<Notification> notifications = mock(List.class);
        when(notificationRepository.findAll()).thenReturn(notifications);
        
        List<Notification> result = notificationService.getNotifications();
        assertEquals(notifications, result);
    }
    
    // 2. getReadNotifications()
    @Test
    void testGetReadNotifications() {
        Long memberId = 1L;
        List<NotificationDTO> readList = mock(List.class);
        when(notificationMapper.getReadNotifications(memberId)).thenReturn(readList);
        
        List<NotificationDTO> result = notificationService.getReadNotifications(memberId);
        assertEquals(readList, result);
    }
    
    // 3. getUnreadNotifications()
    @Test
    void testGetUnreadNotifications() {
        Long memberId = 1L;
        List<NotificationDTO> unreadList = mock(List.class);
        when(notificationMapper.getUnreadNotifications(memberId)).thenReturn(unreadList);
        
        List<NotificationDTO> result = notificationService.getUnreadNotifications(memberId);
        assertEquals(unreadList, result);
    }
    
    // 4. getNotificationsBySenderId()
    @Test
    void testGetNotificationsBySenderId() {
        Long senderId = 10L;
        // 도메인 객체 Notification은 new 대신 @Mock/spy 사용
        Notification notificationMock = mock(Notification.class);
        when(notificationMock.getNotificationId()).thenReturn(1L);
        LocalDateTime createdDate = LocalDateTime.of(2025, 1, 1, 12, 0);
        when(notificationMock.getCreatedDate()).thenReturn(createdDate);
        when(notificationMock.getCategory()).thenReturn("CATEGORY");
        when(notificationMock.getServiceCtg()).thenReturn("SERVICE");
        when(notificationMock.getContent()).thenReturn("Content");
        when(notificationMock.getSenderId()).thenReturn(10L);
        when(notificationMock.getRecipientId()).thenReturn(20L);
        
        List<Notification> notificationList = List.of(notificationMock);
        when(notificationRepository.findAllBySenderIdAndOpenStatusOrderByCreatedDateDesc(senderId, "Y"))
            .thenReturn(notificationList);
        
        List<NotificationDTO> result = notificationService.getNotificationsBySenderId(senderId);
        assertNotNull(result);
        assertEquals(1, result.size());
        NotificationDTO dto = result.get(0);
        assertEquals(1L, dto.getNotificationId());
        assertEquals(createdDate, dto.getCreatedDate());
        assertEquals("CATEGORY", dto.getCategory());
        assertEquals("SERVICE", dto.getServiceCtg());
        assertEquals("Content", dto.getContent());
        assertEquals(10L, dto.getSenderId());
        assertEquals(20L, dto.getRecipientId());
    }
    
    // 5. sendNotification()
    @Test
    void testSendNotification() {
        // 도메인 객체 Notification은 @Mock으로 생성
        Notification notificationMock = mock(Notification.class);
        when(notificationMock.getRecipientId()).thenReturn(20L);
        
        // stub unread notifications from notificationMapper.getUnreadNotifications()
        List<NotificationDTO> unreadList = mock(List.class);
        when(notificationMapper.getUnreadNotifications(20L)).thenReturn(unreadList);
        
        notificationService.sendNotification(notificationMock);
        
        // verify insertNotification() 호출
        verify(notificationMapper).insertNotification(notificationMock);
        // verify that createdDate was set on the notification
        verify(notificationMock).setCreatedDate(any(LocalDateTime.class));
        // verify that messagingTemplate.convertAndSend() was called with the correct parameters
        verify(messagingTemplate).convertAndSend("/topic/notifications", unreadList);
    }
    
    // 6. readNotification()
    @Test
    void testReadNotification() {
        Long notificationId = 5L;
        Long memberId = 30L;
        List<NotificationDTO> unreadList = mock(List.class);
        when(notificationMapper.getUnreadNotifications(memberId)).thenReturn(unreadList);
        
        notificationService.readNotification(notificationId, memberId);
        verify(notificationMapper).updateNotificationToRead(notificationId);
        verify(messagingTemplate).convertAndSend("/topic/notifications", unreadList);
    }
    
    // 7. deleteNotification()
    @Test
    void testDeleteNotification() {
        Long notificationId = 10L;
        doNothing().when(notificationMapper).updateNotificationToDelete(notificationId);
        notificationService.deleteNotification(notificationId);
        verify(notificationMapper).updateNotificationToDelete(notificationId);
    }
}
