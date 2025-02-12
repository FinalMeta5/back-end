package com.hifive.bururung.domain.admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hifive.bururung.domain.admin.dto.AdminCarRegistrationDTO;
import com.hifive.bururung.domain.admin.dto.AdminCarRegistrationRequest;
import com.hifive.bururung.domain.admin.dto.AdminCarShareDTO;
import com.hifive.bururung.domain.admin.dto.AdminMemberDTO;
import com.hifive.bururung.domain.admin.dto.AdminPaymentDTO;
import com.hifive.bururung.domain.admin.dto.AdminTaxiShareDTO;
import com.hifive.bururung.domain.admin.repository.IAdminMapper;

@Service
public class AdminService implements IAdminService {
	
	@Autowired
	private IAdminMapper adminMapper;

	@Override
	public List<AdminMemberDTO> getMemberList() {
		return adminMapper.getMemberList();
	}

	@Override
	public List<AdminCarRegistrationDTO> getRegistrationList() {
		return adminMapper.getRegistrationList();
	}

	@Override
	public List<AdminTaxiShareDTO> getTaxiShareServiceList() {
		return adminMapper.getTaxiShareServiceList();
	}

	@Override
	public List<AdminCarShareDTO> getCarShareServiceList() {
		return adminMapper.getCarShareServiceList();
	}

	@Override
	public List<AdminPaymentDTO> getPaymentList() {
		return adminMapper.getPaymentList();
	}

	@Override
	@Transactional
	public void approveRegistration(AdminCarRegistrationRequest registration) {
		adminMapper.updateRegistrationSetVerifiedToY(registration.getCarId());
		adminMapper.updateMemberToDriver(registration.getMemberId());
	}

}
