package com.ipsdn_opt.app.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class VerifyOptRoute {
    @JsonIgnore
    private long e2enode_id;
    @JsonIgnore
    private long srcNode_id;
    @JsonIgnore
    private String srcNodeName;
    @JsonIgnore
    private long destNode_id;
    @JsonIgnore
    private String destNodeName;
    private String e2e;
    //private List<String> ospf_path = new ArrayList<>();
    private String previous_path;
    private String current_path;

    @JsonIgnore
    private List<AnalysisPath> ospfPaths = new ArrayList<>();
    private List<AnalysisOptPathSet> optimized_path = new ArrayList<>();

    public VerifyOptRoute() {
    }
    public VerifyOptRoute(long e2enode_id, long srcNode_id, String srcNodeName, long destNode_id, String destNodeName) {
        this.e2enode_id = e2enode_id;
        this.srcNode_id = srcNode_id;
        this.srcNodeName = srcNodeName;
        this.destNode_id = destNode_id;
        this.destNodeName = destNodeName;
        this.e2e = srcNodeName + " -> " + destNodeName;
    }
}
