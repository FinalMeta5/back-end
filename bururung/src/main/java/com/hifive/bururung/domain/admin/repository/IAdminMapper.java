package com.hifive.bururung.domain.admin.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.hifive.bururung.domain.admin.dto.AdminCarRegistrationDTO;
import com.hifive.bururung.domain.admin.dto.AdminCarShareDTO;
import com.hifive.bururung.domain.admin.dto.AdminMemberDTO;
import com.hifive.bururung.domain.admin.dto.AdminPaymentDTO;
import com.hifive.bururung.domain.admin.dto.AdminTaxiShareDTO;

@Repository
@Mapper
public interface IAdminMapper {

	List<AdminMemberDTO> getMemberList();

	List<AdminCarRegistrationDTO> getRegistrationList();

	List<AdminTaxiShareDTO> getTaxiShareServiceList();

	List<AdminCarShareDTO> getCarShareServiceList();

	List<AdminPaymentDTO> getPaymentList();

	void updateRegistrationSetVerifiedToY(Long carId);

	void updateMemberToDriver(Long memberId);

	void deleteRegistration(Long carId);

}
