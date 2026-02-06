package com.ipsdn_opt.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipsdn_opt.app.model.Link;

public interface LinkRepository extends JpaRepository<Link, Long> {
    @Query(value = "call findLinkAllUsaged(:faultlink_id);", nativeQuery = true)
    List<Link> findLinkAllUsaged(@Param("faultlink_id") Long faultLink_id);
}
