package com.hifive.bururung.domain.notification.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
	
	@Id
	@SequenceGenerator(
			name = "NOTIFICATION_SEQ_GEN",
			sequenceName = "NOTIFICATION_SEQ",
			allocationSize = 1
	)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTIFICATION_SEQ_GEN")
	private Long notificationId;
	private LocalDateTime createdDate;
	private Long senderId;
	private Long recipientId;
	private String category;
	private String serviceCtg;
    private String content;
    private String openStatus;
    private String checked;
    
//    public Notification(String content) {
//    	this.content = content;
//    }
}
