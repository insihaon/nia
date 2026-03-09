package com.ipsdn_opt.app.model;

import lombok.Data;

@Data
public class ViewLinkFactor {
    private String direction;
    private long link_id;
    private double jitter;
    private double latency;
    private int lost;
    private boolean correct;
    private int hops;
}
