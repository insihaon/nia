package com.ipsdn_opt.app.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "ospfbackupdate")
public class OspfBackupDate {
    @Id
    private long id;
    private LocalDateTime backupdatetime;
}
