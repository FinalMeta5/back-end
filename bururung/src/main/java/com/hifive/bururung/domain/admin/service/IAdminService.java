package com.hifive.bururung.domain.admin.service;

import java.util.List;

import com.hifive.bururung.domain.admin.dto.AdminCarRegistrationDTO;
import com.hifive.bururung.domain.admin.dto.AdminCarRegistrationRequest;
import com.hifive.bururung.domain.admin.dto.AdminCarShareDTO;
import com.hifive.bururung.domain.admin.dto.AdminMemberDTO;
import com.hifive.bururung.domain.admin.dto.AdminPaymentDTO;
import com.hifive.bururung.domain.admin.dto.AdminTaxiShareDTO;

public interface IAdminService {

	List<AdminMemberDTO> getMemberList();

	List<AdminCarRegistrationDTO> getRegistrationList();

	List<AdminTaxiShareDTO> getTaxiShareServiceList();

	List<AdminCarShareDTO> getCarShareServiceList();

	List<AdminPaymentDTO> getPaymentList();

	void approveRegistration(AdminCarRegistrationRequest registration);

}
