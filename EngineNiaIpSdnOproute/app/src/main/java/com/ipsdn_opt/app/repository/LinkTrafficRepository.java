package com.ipsdn_opt.app.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipsdn_opt.app.model.LinkTraffic;

public interface LinkTrafficRepository extends JpaRepository<LinkTraffic, Long> {
    @Query(value = "call findLinkTrafficByDateRange(:fromdate, :todate);", nativeQuery = true)
    List<LinkTraffic> findLinkTrafficByDateRange(@Param("fromdate") LocalDate fromDate, @Param("todate") LocalDate toDate);
}
