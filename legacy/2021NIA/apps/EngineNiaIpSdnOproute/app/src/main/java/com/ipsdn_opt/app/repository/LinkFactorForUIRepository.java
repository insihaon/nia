package com.ipsdn_opt.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipsdn_opt.app.model.LinkFactorForUI;

public interface LinkFactorForUIRepository extends JpaRepository<LinkFactorForUI, Long> {
    @Query(value = "call findLinkFactorForUI();", nativeQuery = true)
    List<LinkFactorForUI> findLinkFactorForUI();

    @Query(value = "call findLinkFactorForUIByNodeid(:node_id);", nativeQuery = true)
    List<LinkFactorForUI> findLinkFactorForUIByNodeid(@Param("node_id") long node_id);

    @Query(value = "call findLinkFactorForMatrix();", nativeQuery = true)
    List<LinkFactorForUI> findLinkFactorForMatrix();

}
