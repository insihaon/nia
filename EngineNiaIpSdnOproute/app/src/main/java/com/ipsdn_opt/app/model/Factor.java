package com.ipsdn_opt.app.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Factor {
    private String nodeName;
    private NodeFactor nodeFactors;
    private List<LinkFactorForUI> linkFactors = new ArrayList<>();

    public Factor() {
    }
    public Factor(String nodeName, NodeFactor nodeFactor, List<LinkFactorForUI> linkFactors) {
        this.nodeName = nodeName;
        this.nodeFactors = nodeFactor;
        this.linkFactors = linkFactors;
    }
}
