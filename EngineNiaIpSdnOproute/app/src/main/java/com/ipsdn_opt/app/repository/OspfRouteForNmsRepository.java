package com.ipsdn_opt.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipsdn_opt.app.model.OspfRouteForNms;

public interface OspfRouteForNmsRepository extends JpaRepository<OspfRouteForNms, Long> {
    @Query(value = "call findOspfRouteByE2eNodeId(:e2enode_id);", nativeQuery = true)
    List<OspfRouteForNms> findOspfRouteByE2eNodeId(@Param("e2enode_id") long e2enode_id);
}
