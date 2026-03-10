package com.ipsdn_opt.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ipsdn_opt.app.model.OspfRoute;

public interface OspfRouteRepository extends JpaRepository<OspfRoute, Long> {

}
