package com.ipsdn_opt.app.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lombok.Data;

@Data
public class Router {
    //private String routerName;
    private long node_id;
    private List<DistanceCost> distances = new ArrayList<>();
    private HashMap<Long, DistanceCost> destMap = new HashMap<>();

    public Router() {
    }
    public Router(long node_id) {
        // this.routerName = routerName;
        this.node_id = node_id;
    }
}
