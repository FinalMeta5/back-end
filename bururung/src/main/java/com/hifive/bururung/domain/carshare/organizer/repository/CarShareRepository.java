package com.hifive.bururung.domain.carshare.organizer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hifive.bururung.domain.carshare.organizer.entity.CarShareRegistration;

public interface CarShareRepository extends JpaRepository<CarShareRegistration, Long>{

}
