package com.hifive.bururung.domain.carshare.organizer.entity;

import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
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
@Table(name = "CAR_SHARE_REGISTRATION")
@SequenceGenerator(
    name = "SEQ_CAR_SHARE_REGI_GEN",
    sequenceName = "SEQ_CAR_SHARE_REGI",
    allocationSize = 1
)
public class CarShareRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CAR_SHARE_REGI_GEN")
    @Column(name = "CAR_SHARE_REGI_ID", nullable = false)
    private Long carShareRegiId;

    @Column(name = "CAR_ID", nullable = false)
    private Long carId;

    @Column(name = "MEMBER_ID", nullable = false)
    private Long memberId;

    @Column(name = "PICKUP_LOC", nullable = false, length = 255)
    private String pickupLoc;

    @Column(name = "LATITUDE_PL", nullable = false)
    private double latitudePl;

    @Column(name = "LONGITUDE_PL", nullable = false)
    private double longitudePl;

    @Column(name = "PASSENGERS_NUM", nullable = false)
    private int passengersNum;

    @Column(name = "CREATED_DATE", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "DESTINATION", nullable = false, length = 255)
    private String destination;

    @Column(name = "LATITUDE_DS", nullable = false)
    private double latitudeDs;

    @Column(name = "LONGITUDE_DS", nullable = false)
    private double longitudeDs;

    @Column(name = "SIDO_PL", length = 20)
    private String sidoPl;

    @Column(name = "SIGUNGU_PL", length = 500)
    private String sigunguPl;

    @Column(name = "ROADNAME_PL", length = 500)
    private String roadNamePl;
    
    @Column(name = "SIDO_DS", length = 20)
    private String sidoDs;

    @Column(name = "SIGUNGU_DS", length = 500)
    private String sigunguDs;

    @Column(name = "ROADNAME_DS", length = 500)
    private String roadNameDs;

    @Column(name = "PICKUP_DATE", nullable = false)
    private LocalDateTime pickupDate;

    @Column(name = "CATEGORY", length = 20)
    private String category;

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }
}
