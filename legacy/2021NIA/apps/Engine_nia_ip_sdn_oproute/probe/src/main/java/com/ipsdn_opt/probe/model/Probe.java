package com.ipsdn_opt.probe.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "probe")
public class Probe {
    @Id
    private long id;
    private String ipaddr;
    private int port;
    private long node_id;
    private boolean automeasurement;
    private int measurementperiod;
    private String pingip;

    @OneToOne
    @JoinColumn(name = "commandserver_id")
    private CommandServer commnadserver;
}
