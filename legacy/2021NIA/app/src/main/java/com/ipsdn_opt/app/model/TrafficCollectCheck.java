package com.ipsdn_opt.app.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class TrafficCollectCheck {
    private long node_id;
    private long if_id;
    private String nodename;
    private String ifname;
    private int fatalErrors;
    private int errCount;
    private LocalDateTime omitedTime_start;
    private LocalDateTime omitedTime_end;
    private LocalDateTime fatal_omitedTime_start;
    private LocalDateTime fatal_omitedTime_end;
    private List<LinkTraffic> linkTraffics = new ArrayList<>();
}
