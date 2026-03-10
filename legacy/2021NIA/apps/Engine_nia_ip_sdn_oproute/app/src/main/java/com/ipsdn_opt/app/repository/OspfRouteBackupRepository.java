package com.ipsdn_opt.app.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipsdn_opt.app.model.OspfRouteBackup;

public interface OspfRouteBackupRepository extends JpaRepository<OspfRouteBackup, Long> {
    @Query(value = "call backupOspfRoute(:backupdatetime);", nativeQuery = true)
    void backupOspfRoute(@Param("backupdatetime") LocalDateTime backupDateTime);
}
