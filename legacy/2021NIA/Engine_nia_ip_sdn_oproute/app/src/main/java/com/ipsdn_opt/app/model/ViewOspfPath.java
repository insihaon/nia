package com.ipsdn_opt.app.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ViewOspfPath {
    private String source_node;
    private String destination_node;
    private List<ViewOspfPathSet> paths = new ArrayList<>();

    public ViewOspfPath() {
    }
    public ViewOspfPath(String source_node, String destination_node) {
        this.source_node = source_node;
        this.destination_node = destination_node;
    }
}
