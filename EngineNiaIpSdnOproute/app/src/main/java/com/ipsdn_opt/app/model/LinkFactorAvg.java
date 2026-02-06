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
@Table(name = "linkfactoravg")
public class LinkFactorAvg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Double latency_avg;
    private Double latency_med;
    @Transient
    private Double latency;
    private Double jitter_avg;
    private Double jitter_med;
    @Transient
    private Double jitter;
    private Integer lost_avg;
    private Integer lost_med;
    @Transient
    private Integer lost;
    @Transient
    private Integer linktrf;
    private Integer avgrange;
    private Integer medposition;
    private Integer modifymetric;
    private long link_id;
    private LocalDate sourcedate;
    public LinkFactorAvg() {
    }
    public LinkFactorAvg(long link_id) {
        this.link_id = link_id;
    }
}
