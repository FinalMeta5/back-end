package com.hifive.bururung.domain.notification.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.hifive.bururung.domain.notification.dto.NotificationDTO;
import com.hifive.bururung.domain.notification.dto.NotificationListResponse;
import com.hifive.bururung.domain.notification.entity.Notification;
import com.hifive.bururung.domain.notification.service.INotificationService;
import com.hifive.bururung.global.util.SecurityUtil;

@ExtendWith(MockitoExtension.class)
public class NotificationControllerTest {

    @InjectMocks
    private NotificationController notificationController;
    
    @Mock
    private INotificationService notificationService;
    
    // 1. GET /api/notifications
    @Test
    void testGetNotifications() {
        List<Notification> notifications = mock(List.class);
        when(notificationService.getNotifications()).thenReturn(notifications);
        
        List<Notification> result = notificationController.getNotifications();
        assertEquals(notifications, result);
    }
    
    // 2. GET /api/notifications/read - 성공 케이스
    @Test
    void testGetReadNotifications_Success() {
        try (MockedStatic<SecurityUtil> securityUtilMock = mockStatic(SecurityUtil.class)) {
            Long memberId = 1L;
            securityUtilMock.when(SecurityUtil::getCurrentMemberId).thenReturn(memberId);
            
            List<NotificationDTO> readList = mock(List.class);
            when(notificationService.getReadNotifications(memberId)).thenReturn(readList);
            
            ResponseEntity<NotificationListResponse<List<NotificationDTO>>> response =
                notificationController.getReadNotifications();
            
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(readList, response.getBody().getData());
            assertNull(response.getBody().getMessage());
        }
    }
    
    // 3. GET /api/notifications/read - 실패(Unauthorized)
    @Test
    void testGetReadNotifications_Unauthorized() {
        try (MockedStatic<SecurityUtil> securityUtilMock = mockStatic(SecurityUtil.class)) {
            String errorMsg = "Session expired";
            securityUtilMock.when(SecurityUtil::getCurrentMemberId)
                             .thenThrow(new IllegalArgumentException(errorMsg));
            
            ResponseEntity<NotificationListResponse<List<NotificationDTO>>> response =
                notificationController.getReadNotifications();
            
            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
            assertNull(response.getBody().getData());
            assertEquals(errorMsg, response.getBody().getMessage());
        }
    }
    
    // 4. GET /api/notifications/unread - 성공 케이스
    @Test
    void testGetUnreadNotifications_Success() {
        try (MockedStatic<SecurityUtil> securityUtilMock = mockStatic(SecurityUtil.class)) {
            Long memberId = 2L;
            securityUtilMock.when(SecurityUtil::getCurrentMemberId).thenReturn(memberId);
            
            List<NotificationDTO> unreadList = mock(List.class);
            when(notificationService.getUnreadNotifications(memberId)).thenReturn(unreadList);
            
            ResponseEntity<NotificationListResponse<List<NotificationDTO>>> response =
                notificationController.getUnreadNotifications();
            
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(unreadList, response.getBody().getData());
            assertNull(response.getBody().getMessage());
        }
    }
    
    // 5. GET /api/notifications/unread - 실패 (Unauthorized)
    @Test
    void testGetUnreadNotifications_Unauthorized() {
        try (MockedStatic<SecurityUtil> securityUtilMock = mockStatic(SecurityUtil.class)) {
            String errorMsg = "Invalid session";
            securityUtilMock.when(SecurityUtil::getCurrentMemberId)
                             .thenThrow(new IllegalStateException(errorMsg));
            
            ResponseEntity<NotificationListResponse<List<NotificationDTO>>> response =
                notificationController.getUnreadNotifications();
            
            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
            assertNull(response.getBody().getData());
            assertEquals(errorMsg, response.getBody().getMessage());
        }
    }
    
    // 6. GET /api/notifications/bysender - 성공 케이스
    @Test
    void testGetNotificationBySenderId_Success() {
        try (MockedStatic<SecurityUtil> securityUtilMock = mockStatic(SecurityUtil.class)) {
            Long memberId = 3L;
            securityUtilMock.when(SecurityUtil::getCurrentMemberId).thenReturn(memberId);
            
            List<NotificationDTO> senderList = mock(List.class);
            when(notificationService.getNotificationsBySenderId(memberId)).thenReturn(senderList);
            
            ResponseEntity<NotificationListResponse<List<NotificationDTO>>> response =
                notificationController.getNotificationBySenderId();
            
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(senderList, response.getBody().getData());
            assertNull(response.getBody().getMessage());
        }
    }
    
    // 7. GET /api/notifications/bysender - 실패 (Unauthorized)
    @Test
    void testGetNotificationBySenderId_Unauthorized() {
        try (MockedStatic<SecurityUtil> securityUtilMock = mockStatic(SecurityUtil.class)) {
            String errorMsg = "No session";
            securityUtilMock.when(SecurityUtil::getCurrentMemberId)
                             .thenThrow(new IllegalArgumentException(errorMsg));
            
            ResponseEntity<NotificationListResponse<List<NotificationDTO>>> response =
                notificationController.getNotificationBySenderId();
            
            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
            assertNull(response.getBody().getData());
            assertEquals(errorMsg, response.getBody().getMessage());
        }
    }
    
    // 8. PUT /api/notifications/{notificationId} - readNotification 성공
    @Test
    void testReadNotification_Success() {
        try (MockedStatic<SecurityUtil> securityUtilMock = mockStatic(SecurityUtil.class)) {
            Long memberId = 4L;
            Long notificationId = 10L;
            securityUtilMock.when(SecurityUtil::getCurrentMemberId).thenReturn(memberId);
            
            List<NotificationDTO> readList = mock(List.class);
            // Simulate that after marking as read, service returns updated read notifications.
            when(notificationService.getReadNotifications(memberId)).thenReturn(readList);
            
            ResponseEntity<NotificationListResponse<List<NotificationDTO>>> response =
                notificationController.readNotification(notificationId);
            
            // Verify that readNotification() on service was called with the correct parameters
            verify(notificationService).readNotification(notificationId, memberId);
            
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(readList, response.getBody().getData());
            assertNull(response.getBody().getMessage());
        }
    }
    
    // 9. PUT /api/notifications/{notificationId} - readNotification 실패 (Unauthorized)
    @Test
    void testReadNotification_Unauthorized() {
        Long notificationId = 10L;
        String errorMsg = "Session error";
        try (MockedStatic<SecurityUtil> securityUtilMock = mockStatic(SecurityUtil.class)) {
            securityUtilMock.when(SecurityUtil::getCurrentMemberId)
                             .thenThrow(new IllegalArgumentException(errorMsg));
            
            ResponseEntity<NotificationListResponse<List<NotificationDTO>>> response =
                notificationController.readNotification(notificationId);
            
            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
            assertNull(response.getBody().getData());
            assertEquals(errorMsg, response.getBody().getMessage());
        }
    }
    
    // 10. DELETE /api/notifications/{notificationId} - 성공
    @Test
    void testDeleteNotification_Success() {
        Long notificationId = 20L;
        doNothing().when(notificationService).deleteNotification(notificationId);
        
        ResponseEntity<String> response = notificationController.deleteNotification(notificationId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("ok", response.getBody());
    }
    
    // 11. DELETE /api/notifications/{notificationId} - 실패
    @Test
    void testDeleteNotification_Failure() {
        Long notificationId = 20L;
        String exceptionMsg = "Delete error";
        doThrow(new RuntimeException(exceptionMsg)).when(notificationService).deleteNotification(notificationId);
        
        ResponseEntity<String> response = notificationController.deleteNotification(notificationId);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(exceptionMsg, response.getBody());
    }
}
