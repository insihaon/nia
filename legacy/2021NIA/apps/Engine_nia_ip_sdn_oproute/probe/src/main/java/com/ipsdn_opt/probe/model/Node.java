package com.ipsdn_opt.probe.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "node")
public class Node {
    @Id
    private long id;
    private String nodename;
    private String loopbackaddr;
    private String mgmtaddr;
}
