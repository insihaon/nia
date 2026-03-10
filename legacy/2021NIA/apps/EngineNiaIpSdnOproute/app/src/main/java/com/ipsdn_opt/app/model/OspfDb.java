package com.ipsdn_opt.app.model;

import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "ospfdb")
public class OspfDb {
    @Id
    private String linkstateid;
    private String advrouter;
    private LocalDateTime createddate;

    public OspfDb(String linkstateid, String advrouter) {
        this.linkstateid = linkstateid;
        this.advrouter = advrouter;
        this.createddate = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }
    public OspfDb() {
        this.createddate = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }
}
