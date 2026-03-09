package com.ipsdn_opt.app.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class CpuMemUsageCheck {
    private long node_id;
    private String nodeName;
    private boolean cpuWarning;
    private boolean memWarning;
    private List<CpuUsage> cpuUsages = new ArrayList<>();
    private List<MemUsage> memUsages = new ArrayList<>();
    public CpuMemUsageCheck() {
    }
    public CpuMemUsageCheck(long node_id, String nodeName) {
        this.node_id = node_id;
        this.nodeName = nodeName;
    }
}
