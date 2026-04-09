package com.ipsdn_opt.app.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipsdn_opt.app.model.OspfDbRouterBackup;

public interface OspfDbRouterBackupRepository extends JpaRepository<OspfDbRouterBackup, Long> {
    @Query(value = "call backupOspfDbRouter(:backupdatetime);", nativeQuery = true)
    void backupOspfDbRouter(@Param("backupdatetime") LocalDateTime backupDateTime);
}
