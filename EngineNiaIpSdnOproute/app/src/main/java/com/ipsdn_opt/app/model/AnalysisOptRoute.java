package com.ipsdn_opt.app.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class AnalysisOptRoute {
    @JsonIgnore
    private long srcNode_id;
    @JsonIgnore
    private String srcNodeName;
    @JsonIgnore
    private long destNode_id;
    @JsonIgnore
    private String destNodeName;
    private String direction;
    //private List<String> ospf_path = new ArrayList<>();
    private String ospf_path;

    @JsonIgnore
    private List<AnalysisPath> ospfPaths = new ArrayList<>();
    private List<AnalysisOptPathSet> optimized_path = new ArrayList<>();

    private List<AnalysisLink> linkFactors = new ArrayList<>();

    public AnalysisOptRoute() {
    }
    public AnalysisOptRoute(long srcNode_id, String srcNodeName, long destNode_id, String destNodeName) {
        this.srcNode_id = srcNode_id;
        this.srcNodeName = srcNodeName;
        this.destNode_id = destNode_id;
        this.destNodeName = destNodeName;
        this.direction = srcNodeName + " -> " + destNodeName;
    }
}
