package com.ipsdn_opt.app.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipsdn_opt.app.model.LinkFactorRouteAvg;

public interface LinkFactorRouteAvgRepository extends JpaRepository<LinkFactorRouteAvg, Long> {
    @Query(value = "call updateRouteAverage(:srcdate, :jitter_avg, :traffic_avg);", nativeQuery = true)
    void updateRouteAverage(@Param("srcdate") LocalDate sourceDate, 
        @Param("jitter_avg") double jitterAvg, @Param("traffic_avg") Integer trafficAvg);
}
