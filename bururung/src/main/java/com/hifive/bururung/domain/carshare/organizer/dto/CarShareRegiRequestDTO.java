package com.hifive.bururung.domain.carshare.organizer.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class CarShareRegiRequestDTO {
    private String pickupLoc;
    private double latitudePl;
    private double longitudePl;
    private String sidoPl;
    private String sigunguPl;
    private String roadnamePl;

    private String destination;
    private double latitudeDs;
    private double longitudeDs;
    private String sidoDs;
    private String sigunguDs;
    private String roadnameDs;

    private int passengersNum;
    private String pickupDate;
    private String category;
    
    @Override
    public String toString() {
        return "CarShareRegiRequestDTO{" +
                "pickupLoc='" + pickupLoc + '\'' +
                ", latitudePl=" + latitudePl +
                ", longitudePl=" + longitudePl +
                ", sidoPl='" + sidoPl + '\'' +
                ", sigunguPl='" + sigunguPl + '\'' +
                ", roadnamePl='" + roadnamePl + '\'' +
                ", destination='" + destination + '\'' +
                ", latitudeDs=" + latitudeDs +
                ", longitudeDs=" + longitudeDs +
                ", sidoDs='" + sidoDs + '\'' +
                ", sigunguDs='" + sigunguDs + '\'' +
                ", roadnameDs='" + roadnameDs + '\'' +
                ", passengersNum=" + passengersNum +
                ", pickupDate='" + pickupDate + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
