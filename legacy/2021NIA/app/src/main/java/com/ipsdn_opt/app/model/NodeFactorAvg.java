package com.ipsdn_opt.app.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

@Data
@Entity
@Table(name = "nodefactoravg")
public class NodeFactorAvg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Float cpuusage_avg;
    private Float cpuusage_med;
    @Transient
    private Float cpuusage;
    private Float memusage_avg;
    private Float memusage_med;
    @Transient
    private Float memusage;
    private Integer avgrange;
    private Integer medposition;
    private long node_id;
    private LocalDate sourcedate;
    public NodeFactorAvg() {
    }
    public NodeFactorAvg(long node_id) {
        this.node_id = node_id;
    }
}
