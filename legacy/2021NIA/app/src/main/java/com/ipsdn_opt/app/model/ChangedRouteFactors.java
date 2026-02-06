package com.ipsdn_opt.app.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class ChangedRouteFactors {
    @Id
    private long id;
    private long routepathset_id;
    private long link_id;
    private String optsource;
    private LocalDate sourcedate;
    private double latency_standard;
    private double jitter_route_avg;
    private Integer traffic_route_avg;
    private double latency_avg;
    private double latency_med;
    private double jitter_avg;
    private double jitter_med;
    private Integer traffic_avg;
    private Integer traffic_med;
    private Integer avgrange;
    private Integer medposition;
    private Integer ospfmetric;
    private Integer modifymetric;
}
