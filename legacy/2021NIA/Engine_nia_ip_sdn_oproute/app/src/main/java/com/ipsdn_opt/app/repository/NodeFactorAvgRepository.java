package com.ipsdn_opt.app.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipsdn_opt.app.model.NodeFactorAvg;

public interface NodeFactorAvgRepository extends JpaRepository<NodeFactorAvg, Long> {
    @Query(value = "call deleteNodeFactorAvgBySourcedate(:fromdate); ", nativeQuery = true)
    void deleteNodeFactorAvgBySourcedate(@Param("fromdate") LocalDate fromDate);

    
    @Query(value = "call findNodeFactorAvgBySourcedate(:sourceDate); ", nativeQuery = true)
    List<NodeFactorAvg> findNodeFactorAvgBySourcedate(@Param("sourceDate") LocalDate sourceDate);
}
