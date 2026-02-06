package com.ipsdn_opt.probe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipsdn_opt.probe.model.Probe;

public interface ProbeRepository extends JpaRepository<Probe, Long> {
    @Query(value = "call findProbeByNodeid(:node_id); ", nativeQuery = true)
    Probe findProbebyNodeid(@Param("node_id") long node_id);
}
