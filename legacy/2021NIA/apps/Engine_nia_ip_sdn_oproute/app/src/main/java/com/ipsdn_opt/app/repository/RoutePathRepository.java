package com.ipsdn_opt.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ipsdn_opt.app.model.RoutePath;

public interface RoutePathRepository extends JpaRepository<RoutePath, Long> {
    
}
