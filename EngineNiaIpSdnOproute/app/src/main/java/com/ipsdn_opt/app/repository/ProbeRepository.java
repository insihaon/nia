package com.ipsdn_opt.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipsdn_opt.app.model.Probe;

public interface ProbeRepository extends JpaRepository<Probe, Long> {
    @Query(value = "call findProbe4ColbyNodeid(:node_id) ", nativeQuery = true)
    Probe findProbe4ColbyNodeid(@Param("node_id") long node_id);


}
