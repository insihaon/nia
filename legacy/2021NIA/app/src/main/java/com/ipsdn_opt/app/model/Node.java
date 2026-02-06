package com.ipsdn_opt.app.model;

import java.util.HashMap;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

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

    @Transient
    private HashMap<String, Interface> ifMap = new HashMap<>();
}
