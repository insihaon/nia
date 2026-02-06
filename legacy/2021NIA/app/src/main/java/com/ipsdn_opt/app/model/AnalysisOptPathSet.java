package com.ipsdn_opt.app.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class AnalysisOptPathSet {
    private int optimized_count;
    @JsonIgnore
    private List<AnalysisPath> optPaths = new ArrayList<>();
    //private List<String> paths = new ArrayList<>();
    private String paths;

    public AnalysisOptPathSet() {
    }
    public AnalysisOptPathSet(int optimized_count) {
        this.optimized_count = optimized_count;
    }
}
