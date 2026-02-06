package com.ipsdn_opt.app.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "probe")
public class Probe {
    @Id
    private long id;
    private String ipaddr;
    private Integer port;
    private long node_id;
}
