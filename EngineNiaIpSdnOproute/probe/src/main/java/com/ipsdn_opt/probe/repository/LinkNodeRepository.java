package com.ipsdn_opt.probe.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipsdn_opt.probe.model.LinkNode;

public interface LinkNodeRepository extends JpaRepository<LinkNode, Long> {
    @Query(value = "call findLinkNodeByNodeid(:snode_id); ", nativeQuery = true)
    List<LinkNode> findLinkNodeByNodeid(@Param("snode_id") long snode_id);
    
    @Query(value = "call findOspfPathLink(:snode_id, :rnode_id); ", nativeQuery = true)
    List<LinkNode> findOspfPathLink(@Param("snode_id") long snode_id, @Param("rnode_id") long rnode_id);
}
