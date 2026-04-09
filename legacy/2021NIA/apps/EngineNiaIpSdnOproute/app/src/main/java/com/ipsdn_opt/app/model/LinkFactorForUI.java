package com.ipsdn_opt.app.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name = "linkfactor")
public class LinkFactorForUI {
    @Id
    @JsonIgnore
    private long id;
    private long link_id;
    private String srcnodename;
    private String destnodename;
    private double latency;
    private double jitter;
    private int lost;
    private int hops;
    private boolean corrected;
    private LocalDateTime measureddatetime;
}
