package com.ipsdn_opt.app.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class RequestMetricConfig {
    private long node_id;
    private List<MetricInterface> interfaces = new ArrayList<>();

    public RequestMetricConfig() {
    }
    public RequestMetricConfig(long node_id) {
        this.node_id = node_id;
    }
}
