package com.ipsdn_opt.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipsdn_opt.app.model.OptRunDebug;

public interface OptRunDebugRepository extends JpaRepository<OptRunDebug, Long> {
    @Query(value = "call deleteOptRunDebugByRunhisId(:runhis_id);", nativeQuery = true)
    void deleteOptRunDebugByRunhisId(@Param("runhis_id") long runhis_id);
}
