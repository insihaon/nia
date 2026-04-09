package com.ipsdn_opt.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ipsdn_opt.app.model.BgpTable;

public interface BgpTableRepository extends JpaRepository<BgpTable, Long> {
    
}
