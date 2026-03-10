package com.ipsdn_opt.app.model;

import lombok.Data;

@Data
public class MetricInterface {
    private String ifname;
    private Integer metric;

    public MetricInterface() {
    }
    public MetricInterface(String ifname, Integer metric) {
        this.ifname = ifname;
        this.metric = metric;
    }
}
