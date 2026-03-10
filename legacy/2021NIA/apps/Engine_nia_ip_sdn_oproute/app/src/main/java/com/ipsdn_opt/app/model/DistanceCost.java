package com.ipsdn_opt.app.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class DistanceCost {
    //private String destRouter;
    private long destNode_id;
    private int totCost;
    private long sif_id;
    private List<Path> paths = new ArrayList<>();

    public DistanceCost() {
    }
    public DistanceCost(long destNode_id) {
        // this.destRouter = destRouter;
        this.destNode_id = destNode_id;
        this.totCost = Integer.MAX_VALUE;
    }
}
