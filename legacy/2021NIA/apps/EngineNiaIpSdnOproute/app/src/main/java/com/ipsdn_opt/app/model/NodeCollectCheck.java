package com.ipsdn_opt.app.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class NodeCollectCheck {
    private long node_id;
    private String nodename;
    private int fatalErrors;
    private int errCount;
    private List<NodeFactor> nodeFactors = new ArrayList<>();
}
