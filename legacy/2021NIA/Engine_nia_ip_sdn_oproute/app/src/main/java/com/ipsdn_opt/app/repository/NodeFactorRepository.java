package com.ipsdn_opt.app.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipsdn_opt.app.model.NodeFactor;

public interface NodeFactorRepository extends JpaRepository<NodeFactor, Long> {
    @Query(value = "call findNodeFactorByNodeid(:node_id);", nativeQuery = true)
    NodeFactor findNodeFactorByNodeid(@Param("node_id") long node_id);

    @Query(value = "call findNodeFactorByDateRange(:fromdate,:todate);", nativeQuery = true)
    List<NodeFactor> findNodeFactorByDateRange(@Param("fromdate") LocalDate fromDate, @Param("todate") LocalDate toDate);
}
