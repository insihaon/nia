package com.ipsdn_opt.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ipsdn_opt.app.model.E2eNode;

public interface E2eNodeRepository extends JpaRepository<E2eNode, Long> {
    @Query(value = "call findAllE2eNodeUsaged(); ", nativeQuery = true)
    List<E2eNode> findAllE2eNodeUsaged();
}
