package com.ipsdn_opt.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipsdn_opt.app.model.OptRoute;

public interface OptRouteRepository extends JpaRepository<OptRoute, Long> {
    @Query(value = "call deleteOptRouteByRunhisId(:runhis_id);", nativeQuery = true)
    void deleteOptRouteByRunhisId(@Param("runhis_id") long runhis_id);

    @Query(value = "call snapshotOspf(:runhis_id);", nativeQuery = true)
    void snapshotOspf(@Param("runhis_id") long runhis_id);

    @Query(value = "call updateOptimizedRouteApply(:runhis_id, :isapply);", nativeQuery = true)
    void updateOptimizedRouteApply(@Param("runhis_id") long runhis_id, @Param("isapply") boolean isApply);
}
