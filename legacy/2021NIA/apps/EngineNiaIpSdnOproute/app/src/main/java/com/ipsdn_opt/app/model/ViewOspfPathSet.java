package com.ipsdn_opt.app.model;

import lombok.Data;

@Data
public class ViewOspfPathSet {
    int pathOrder;
    String path;

    public ViewOspfPathSet() {
    }
    public ViewOspfPathSet(int pathOrder, String path) {
        this.pathOrder = pathOrder;
        this.path = path;
    }
}
