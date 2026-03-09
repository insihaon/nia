package com.ipsdn_opt.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipsdn_opt.app.model.ConfigMetricHis;

public interface ConfigMetricHisRepository extends JpaRepository<ConfigMetricHis, Long> {
    @Query(value = "call deleteConfigMetricHisByRunhisId(:runhis_id);", nativeQuery = true)
    void deleteConfigMetricHisByRunhisId(@Param("runhis_id") long runhis_id);
}
