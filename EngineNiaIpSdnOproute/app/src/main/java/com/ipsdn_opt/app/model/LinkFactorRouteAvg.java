package com.ipsdn_opt.app.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "linkfactorrouteavg")
public class LinkFactorRouteAvg {
    @Id
    private long id;
    private double latency;
    private double jitter;
    private Integer traffic;
    private LocalDateTime sourcedate;
}
