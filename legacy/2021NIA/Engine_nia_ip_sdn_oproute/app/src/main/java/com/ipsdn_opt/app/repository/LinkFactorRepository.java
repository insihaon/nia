package com.ipsdn_opt.app.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipsdn_opt.app.model.LinkFactor;

public interface LinkFactorRepository extends JpaRepository<LinkFactor, Long> {
    @Query(value = "call findLinkFactorByDateRange(:fromdate, :todate);", nativeQuery = true)
    List<LinkFactor> findLinkFactorByDateRange(@Param("fromdate") LocalDate fromDate, @Param("todate") LocalDate toDate);
}
