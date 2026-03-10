package com.ipsdn_opt.app.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipsdn_opt.app.model.LinkTrafficAvg;

public interface LinkTrafficAvgRepository extends JpaRepository<LinkTrafficAvg, Long> {
    @Query(value = "call deleteLinkTrafficAvgBySourcedate(:fromdate); ", nativeQuery = true)
    void deleteLinkTrafficAvgBySourcedate(@Param("fromdate") LocalDate fromDate);

    @Query(value = "call findLinkTrafficAvgBySourcedate(:sourceDate); ", nativeQuery = true)
    List<LinkTrafficAvg> findLinkTrafficAvgBySourcedate(@Param("sourceDate") LocalDate sourceDate);
}
