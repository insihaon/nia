package com.ipsdn_opt.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ipsdn_opt.app.model.OspfRouteOnly;

public interface OspfRouteOnlyRepository extends JpaRepository<OspfRouteOnly, Long> {
    
}
