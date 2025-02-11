package com.hifive.bururung.domain.carshare.organizer.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
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
}
