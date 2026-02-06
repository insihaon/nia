package com.ipsdn_opt.app.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class LinkForCalc {
    // private String routerName;
    // private List<AdjRouter> adjRouters = new ArrayList<>();
    // private boolean visited = false;
    // public OspfLinkInfo() {
    // }
    // public OspfLinkInfo(String routerName) {
    //     this.routerName = routerName;
    // }
    private long node_id;
    private List<AdjRouter> adjRouters = new ArrayList<>();
    private boolean visited = false;
    public LinkForCalc() {
    }
    public LinkForCalc(long node_id) {
        this.node_id = node_id;
    }
}
