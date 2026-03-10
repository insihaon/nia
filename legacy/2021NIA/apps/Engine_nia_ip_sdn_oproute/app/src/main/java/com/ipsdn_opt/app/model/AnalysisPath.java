package com.ipsdn_opt.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class AnalysisPath implements Comparable<AnalysisPath> {
    private int pathser;
    @JsonIgnore
    private long link_id;
    @JsonIgnore
    private long sendNode_id;
    @JsonIgnore
    private long receiveNode_id;
    @JsonIgnore
    private String sendNodeName;
    @JsonIgnore
    private String receiveNodeName;
    private String paths;

    public AnalysisPath() {
    }
    public AnalysisPath(int pathser, long link_id, long sendNode_id, String sendNodeName, long receiveNode_id, String receiveNodename) {
        this.pathser = pathser;
        this.link_id = link_id;
        this.sendNode_id = sendNode_id;
        this.sendNodeName = sendNodeName;
        this.receiveNode_id = receiveNode_id;
        this.receiveNodeName = receiveNodename;
        this.paths = sendNodeName + " -> " + receiveNodename;
    }

    @Override
    public int compareTo(AnalysisPath analysisPath) {
        if(analysisPath.pathser < pathser) return 1;
        else if(analysisPath.pathser > pathser) return -1;
        return 0;
    }
}
