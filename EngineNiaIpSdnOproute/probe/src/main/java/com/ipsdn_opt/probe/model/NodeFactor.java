package com.ipsdn_opt.probe.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "nodefactor")
public class NodeFactor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private float cpuusage;
    private float memusage;
    private long node_id;
    private LocalDateTime measureddatetime;

    public NodeFactor() {
    }
    public NodeFactor(long node_id, LocalDateTime measureddatetime) {
        this.node_id = node_id;
        this.measureddatetime = measureddatetime;
        this.cpuusage = -1;
        this.memusage = -1;
    }
}
