package com.ipsdn_opt.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class AnalysisE2e {
    private String e2e;
    private boolean isOspf_path_symmetric = true;
    private boolean isOptimized_path_symmetric = false;
    private AnalysisOptRoute forward;
    private AnalysisOptRoute backward;
    @JsonIgnore
    private boolean isRemove = false;

    public AnalysisE2e() {
    }
    public AnalysisE2e(String e2e) {
        this.e2e = e2e;
    }
}
