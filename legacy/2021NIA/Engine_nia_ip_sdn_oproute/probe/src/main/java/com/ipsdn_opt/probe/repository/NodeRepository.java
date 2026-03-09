package com.ipsdn_opt.probe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ipsdn_opt.probe.model.Node;

public interface NodeRepository extends JpaRepository<Node, Long> {
}
