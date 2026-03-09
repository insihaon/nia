package com.ipsdn_opt.app.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipsdn_opt.app.model.OptRunHis;

public interface OptRunHisRepository extends JpaRepository<OptRunHis, Long> {
    @Query(value = "call findOptRunHisBySourcedate(:sourceDate);", nativeQuery = true)
    OptRunHis findOptRunHisBySourcedate(@Param("sourceDate") LocalDate sourceDate);
}
