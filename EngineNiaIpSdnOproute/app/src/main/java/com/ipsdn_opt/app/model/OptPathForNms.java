package com.ipsdn_opt.app.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class OptPathForNms {
    private Integer count;
    private Boolean usaged;
    private long routepathset_id;
    private List<RoutePathForNms> paths = new ArrayList<>();

    public OptPathForNms() {
    }
    public OptPathForNms(long routepathset_id, Integer count, Boolean usaged) {
        this.routepathset_id = routepathset_id;
        this.count = count;
        this.usaged = usaged;
    }
}
