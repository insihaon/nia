package com.ipsdn_opt.app.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipsdn_opt.app.model.LinkFactorAvg;

public interface LinkFactorAvgRepository extends JpaRepository<LinkFactorAvg, Long> {
    @Query(value = "call deleteLinkFactorAvgBySourcedate(:fromdate); ", nativeQuery = true)
    void deleteLinkFactorAvgBySourcedate(@Param("fromdate") LocalDate fromDate);

    @Query(value = "call findLinkFactorAvgBySourcedate(:fromdate); ", nativeQuery = true)
    List<LinkFactorAvg> findLinkFactorAvgBySourcedate(@Param("fromdate") LocalDate fromDate);

    @Query(value = "call updateMetricOfLinkFactorAvgById(:linkfactoravg_id, :metric);", nativeQuery = true)
    void updateMetricOfLinkFactorAvgById(@Param("linkfactoravg_id") long linkfactoravg_id, @Param("metric") Integer metric);
}
