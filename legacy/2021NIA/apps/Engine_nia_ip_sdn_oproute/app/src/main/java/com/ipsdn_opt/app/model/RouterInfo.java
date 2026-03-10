package com.ipsdn_opt.app.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "routerinfo")
public class RouterInfo {
    @Id
    private long id;
    private String routername;
    private String ipaddress;
    private String loginid;
    private String password;
    private int port;
    private String prompt;
}
