package com.ipsdn_opt.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ipsdn_opt.app.model.OspfDbRouter;

public interface OspfDbRouterRepository extends JpaRepository<OspfDbRouter, Long> {
    
}
