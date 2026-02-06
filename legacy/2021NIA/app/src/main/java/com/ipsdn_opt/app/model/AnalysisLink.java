package com.ipsdn_opt.app.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class AnalysisLink {
    private long link_id;
    private String link;
    private List<AnalysisFactors> factors = new ArrayList<>();

    public AnalysisLink() {
    }
    public AnalysisLink(long link_id, String link) {
        this.link_id = link_id;
        this.link = link;
    }
}
