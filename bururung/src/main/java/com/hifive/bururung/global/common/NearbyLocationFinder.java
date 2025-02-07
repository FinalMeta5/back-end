package com.hifive.bururung.global.common;

import java.util.ArrayList;
import java.util.List;

import com.hifive.bururung.domain.taxi.dto.LatLng;

public class NearbyLocationFinder {
	
    // 걸어갈 수 있는 거리 (km)
    private static final double WALKABLE_DISTANCE = 2.5;
    
    public static List<LatLng> findNearbyLocations(LatLng center, List<LatLng> allLocations) {
        List<LatLng> nearbyLocations = new ArrayList<>();
        
        // 위도 1도당 거리 (km)
        double kmPerLatitude = 111.0;
        
        // 경도 1도당 거리 (km)
        double kmPerLongitude = 111.0 * Math.cos(Math.toRadians(center.getLat()));
        
        // 위도 범위 계산
        double latitudeDelta = WALKABLE_DISTANCE / kmPerLatitude;
        double minLatitude = center.getLat() - latitudeDelta;
        double maxLatitude = center.getLat() + latitudeDelta;
        
        // 경도 범위 계산
        double longitudeDelta = WALKABLE_DISTANCE / kmPerLongitude;
        double minLongitude = center.getLng() - longitudeDelta;
        double maxLongitude = center.getLng() + longitudeDelta;
        
        // 모든 위치를 순회하며 범위 내에 있는 위치 찾기
        for (LatLng location : allLocations) {
            if (location.getLat() >= minLatitude && location.getLat() <= maxLatitude &&
                location.getLng() >= minLongitude && location.getLng() <= maxLongitude) {
                
                // 더 정확한 거리 계산을 위해 실제 거리 확인 (선택사항)
                if (calculateDistance(center, location) <= WALKABLE_DISTANCE) {
                    nearbyLocations.add(location);
                }
            }
        }
        
        return nearbyLocations;
    }
    
    // 두 지점 간의 거리를 계산하는 메소드 (Haversine 공식 사용)
    private static double calculateDistance(LatLng point1, LatLng point2) {
        double earthRadius = 6371; // 지구의 반지름 (km)
        
        double latDistance = Math.toRadians(point2.getLat() - point1.getLat());
        double lngDistance = Math.toRadians(point2.getLng() - point1.getLng());
        
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                 + Math.cos(Math.toRadians(point1.getLat())) * Math.cos(Math.toRadians(point2.getLat()))
                 * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return earthRadius * c;
    }
//    public static void main(String[] args) {
//        // 예시 사용
//        LatLng center = new LatLng(37.5665, 126.9780); // 서울시청 좌표
//        
//        List<LatLng> allLocations = new ArrayList<>();
//        allLocations.add(new LatLng(37.5662, 126.9784)); // 근처 위치
//        allLocations.add(new LatLng(37.5664, 126.9779)); // 근처 위치
//        allLocations.add(new LatLng(37.5666, 126.9783)); // 근처 위치
//        allLocations.add(new LatLng(38.5666, 127.9783)); // 먼 위치
//        
//        List<LatLng> nearbyLocations = findNearbyLocations(center, allLocations);
//        
//        System.out.println("걸어갈 수 있는 거리 내의 위치 수: " + nearbyLocations.size());
//        for (LatLng location : nearbyLocations) {
//            System.out.println("위도: " + location.lat + ", 경도: " + location.lng);
//        }
//    }
}
