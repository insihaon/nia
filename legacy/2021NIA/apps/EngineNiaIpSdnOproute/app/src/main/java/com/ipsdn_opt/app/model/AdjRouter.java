package com.ipsdn_opt.app.model;

import lombok.Data;

@Data
public class AdjRouter {
    // private String routerName;
    // private String interfaceName;
    private long node_id;
    private long interface_id;
    private long hwinterface_id;
    private int metric;

    public AdjRouter() {
    }
    public AdjRouter(long node_id, long interface_id, long hwinterface_id, int metric) {
        // this.routerName = routerName;
        // this.interfaceName = interfaceName;
        this.node_id = node_id;
        this.interface_id = interface_id;
        this.hwinterface_id = hwinterface_id;
        this.metric = metric;
    }
}
