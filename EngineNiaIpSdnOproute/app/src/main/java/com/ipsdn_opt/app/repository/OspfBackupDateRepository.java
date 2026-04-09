package com.ipsdn_opt.app.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipsdn_opt.app.model.OspfBackupDate;

public interface OspfBackupDateRepository extends JpaRepository<OspfBackupDate, Long> {
    @Query(value = "call addOspfBackupDate(:backupdatetime); ", nativeQuery = true)
    void addOspfBackupDate(@Param("backupdatetime") LocalDateTime backupDateTime);
}
