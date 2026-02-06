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
@Table(name = "linkfactor")
public class LinkFactor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Double latency;
    private Double jitter;
    private Integer lost;
    private Integer hops;
    private long link_id;
    private boolean corrected;
    LocalDateTime measureddatetime;

    public LinkFactor() {
    }
    public LinkFactor(long link_id, LocalDateTime measureddatetime) {
        this.link_id = link_id;
        this.measureddatetime = measureddatetime;
        this.latency = -1.0;
        this.jitter = -1.0;
        this.corrected = false;
    }
}
