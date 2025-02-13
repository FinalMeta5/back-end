package com.hifive.bururung.domain.carshare.organizer.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.hifive.bururung.domain.carshare.organizer.dto.MyCarServiceParticipantListResponseDTO;

@Mapper
@Repository
public interface CarShareMapper {
	// 내 차량 공유 서비스의 참가자 리스트 조회
	List<MyCarServiceParticipantListResponseDTO> findMyCarServiceParticipantList(Long carShareRegiId);

    // car_share 테이블에서 특정 carId가 존재하는지 확인
    int countByCarId(@Param("carId") Long carId);

    // car_share 테이블에서 특정 carId를 0으로 변경
    void updateCarIdToZero(@Param("carId") Long carId);
}
