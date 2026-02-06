package com.ipsdn_opt.app.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class NodeUsageRate {
    private long node_id;
    private String nodeName;
    private List<UsageRate> usageRates = new ArrayList<>();

    public NodeUsageRate(long node_id, String nodeName) {
        this.node_id = node_id;
        this.nodeName = nodeName;
    }
}
