package com.ipsdn_opt.app.model;

import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "bgptable")
public class BgpTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String prefix;
    private String nexthop;
    private LocalDateTime createddate;

    public BgpTable() {
        this.createddate = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }
    public BgpTable(String prefix, String nexthop) {
        this.prefix = prefix;
        this.nexthop = nexthop;
        this.createddate = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }
}
