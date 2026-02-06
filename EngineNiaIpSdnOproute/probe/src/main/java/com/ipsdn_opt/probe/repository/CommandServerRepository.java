package com.ipsdn_opt.probe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipsdn_opt.probe.model.CommandServer;

public interface CommandServerRepository extends JpaRepository<CommandServer, Long> {
    @Query(value = "call findCommandServerById(:commandserver_id);", nativeQuery = true)
    CommandServer findCommandServerById(@Param("commandserver_id") long commandserver_id);

    @Query(value = "call findPasswordById(:commandserver_id);", nativeQuery = true)
    String findPasswordById(@Param("commandserver_id") long commandserver_id);
}
