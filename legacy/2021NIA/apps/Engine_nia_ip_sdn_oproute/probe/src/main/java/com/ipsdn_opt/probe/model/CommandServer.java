package com.ipsdn_opt.probe.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "commandserver")
public class CommandServer {
    @Id
    private long id;
    private String ipaddr;
    private int sshport;
    private String loginid;
    private String password;
    private String namespace;
    private String prompt;
    private String serverdesc;
}
