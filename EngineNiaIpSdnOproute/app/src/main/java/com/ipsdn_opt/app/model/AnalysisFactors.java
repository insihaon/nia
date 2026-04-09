package com.ipsdn_opt.app.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class AnalysisFactors {
    private LocalDate sourceDate;
    private double latency_standard;
    private double latency;
    private double jitter_avg;
    private double jitter;
    private Integer traffic_avg;
    private Integer traffic;
    private Integer ospf_metric;
    private Integer modified_metric;

    public AnalysisFactors() {
    }
    public AnalysisFactors(LocalDate sourceDate, double latency_standard, double latency,
            double jitter_avg, double jitter, Integer traffic_avg, Integer traffic, Integer ospf_metric, Integer modified_metric) {
        this.sourceDate = sourceDate;
        this.latency_standard = latency_standard;
        this.latency = latency;
        this.jitter_avg = jitter_avg;
        this.jitter = jitter;
        this.traffic_avg = traffic_avg;
        this.traffic = traffic;
        this.ospf_metric = ospf_metric;
        this.modified_metric = modified_metric;        
    }
}
