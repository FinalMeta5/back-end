package com.hifive.bururung.domain.taxi.entity;

import static jakarta.persistence.FetchType.LAZY;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.hifive.bururung.domain.member.entity.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TAXI_SHARE")
public class TaxiShareEntity {
	
	@Id
	@SequenceGenerator(
			name = "TAXI_SHARE_SEQ_GEN",
			sequenceName = "TAXI_SHARE_SEQ",
			allocationSize = 1
	)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TAXI_SHARE_SEQ_GEN")
	private Long taxiShareId;
	
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
	private Member member;
    
	private Integer passengersNum;
	
	private String pickupLocation;
	
	@Column(name = "LATITUDE_PL")
	private Double latitudePL;
	
	@Column(name = "LONGITUDE_PL")
	private Double longitudePL;
	
	private Character status;
	
	private LocalDateTime createdDate;
	
	private String destination;
	
	@Column(name = "LATITUDE_DS")
	private Double latitudeDS;
	
	@Column(name = "LONGITUDE_DS")
	private Double longitudeDS;
	
	private String openchatLink;
	
	private String openchatCode;
	
	private Integer estimatedAmount;
	
	private String timeNego;
	
	@Version
	private Integer version;
	
}
