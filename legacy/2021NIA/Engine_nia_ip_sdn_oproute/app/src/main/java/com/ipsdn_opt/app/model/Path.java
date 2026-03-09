package com.ipsdn_opt.app.model;

import lombok.Data;

@Data
public class Path {
    private long sendif_id;
    Link link;

    public Path() {
    }
    public Path(long sendif_id, Link link) {
        this.sendif_id = sendif_id;
        this.link = link;
    }

}
