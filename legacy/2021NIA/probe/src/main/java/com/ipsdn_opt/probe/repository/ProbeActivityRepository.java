package com.ipsdn_opt.probe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ipsdn_opt.probe.model.ProbeActivity;

public interface ProbeActivityRepository extends JpaRepository<ProbeActivity, Long> {
    
}
