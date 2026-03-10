package com.ipsdn_opt.app.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class OptRouteForNms {
    private long e2enode_id;
    @JsonIgnore
    private HashMap<Long, OptPathForNms> mapOptPath = new HashMap<>();
    private List<OptPathForNms> optimized_paths = new ArrayList<>();

    public OptRouteForNms() {
    }
    public OptRouteForNms(long e2enode_id) {
        this.e2enode_id = e2enode_id;
    }
}
