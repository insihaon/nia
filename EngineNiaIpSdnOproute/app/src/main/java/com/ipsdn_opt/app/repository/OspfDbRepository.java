package com.ipsdn_opt.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ipsdn_opt.app.model.OspfDb;

public interface OspfDbRepository extends JpaRepository<OspfDb, String> {
    
}
