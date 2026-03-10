package com.ipsdn_opt.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipsdn_opt.app.model.Node;

public interface NodeRepository extends JpaRepository<Node, Long> {
    @Query(name = "call findNodeByNodename(:nodename);", nativeQuery = true)
    Node findNodeByNodename(@Param("nodename") String nodename);

    @Query(value = "select id, nodename, loopbackaddr, mgmtaddr from node where id in (1,2,5,6) ", nativeQuery = true)
    List<Node> findSomeNodes();

    @Query(value = "call updateNmsId();", nativeQuery = true)
    void updateNmsId();
}
