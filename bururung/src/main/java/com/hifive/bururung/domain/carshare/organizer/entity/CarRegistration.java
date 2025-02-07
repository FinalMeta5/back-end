package com.hifive.bururung.domain.carshare.organizer.entity;

import java.time.LocalDateTime;

import com.hifive.bururung.domain.member.entity.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "CAR_REGISTRATION")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class CarRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAR_REG_SEQ")
    @SequenceGenerator(name = "CAR_REG_SEQ", sequenceName = "SEQ_CAR_REGISTRATION", allocationSize = 1)
    @Column(name = "CAR_ID", nullable = false)
    private Long carId;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;  // MEMBER 테이블의 MEMBER_ID를 참조하는 관계

    @Column(name = "CAR_MODEL", nullable = false, length = 50)
    private String carModel;

    @Column(name = "MAX_PASSENGERS", nullable = false)
    private int maxPassengers;

    @Column(name = "CAR_NUMBER", nullable = false, length = 20)
    private String carNumber;

    @Lob
    @Column(name = "CAR_DESCRIPTION")
    private String carDescription;

    @Column(name = "COLOR", nullable = false, length = 10)
    private String color;

    @Column(name = "VERIFIED", nullable = false, length = 1, columnDefinition = "CHAR(1) DEFAULT 'N'")
    private String verified = "N"; // 기본값 'N'

    @Column(name = "CREATED_DATE", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "IMAGE_URL", nullable = false, length = 500)
    private String imageUrl;

    @Column(name = "IMAGE_NAME", nullable = false, length = 500)
    private String imageName;
    
    @Column(name = "VERIFIED_FILE", nullable = false, length = 500)
    private String verifiedFile;

    // INSERT 시 현재 시간을 자동 설정
    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }
}
