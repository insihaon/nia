package com.ipsdn_opt.app.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "ospfdbrouter_backup")
public class OspfDbRouterBackup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String linkstateid;
    private String nrouteraddr;
    private String ifnetaddr;
    private int tos0metric;
    private LocalDateTime createddatetime;

    public OspfDbRouterBackup() {
    }
    public OspfDbRouterBackup(String linkstateid, String nrouteraddr) {
        this.linkstateid = linkstateid;
        this.nrouteraddr = nrouteraddr;
    }
}
