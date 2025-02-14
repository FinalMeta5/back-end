package com.hifive.bururung.domain.carshare.participant.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "CAR_SHARE_JOIN")
@SequenceGenerator(
    name = "SEQ_CAR_SHARE_JOIN_GEN",
    sequenceName = "SEQ_CAR_SHARE_JOIN", 
    allocationSize = 1
)
public class CarShareJoin {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CAR_SHARE_JOIN_GEN")  
    @Column(name = "CAR_SHARE_JOIN_ID", nullable = false)
    private Long carShareJoinId;
    
    

    @Column(name = "MEMBER_ID", nullable = false)
    private Long memberId;

    @Column(name = "STATE", nullable = false, length = 50)
    private String state;

    @Column(name = "JOIN_DATE", nullable = false)
    private LocalDateTime joinDate;

    @PrePersist
    protected void onCreate() {
        this.joinDate = LocalDateTime.now();  
    }
}