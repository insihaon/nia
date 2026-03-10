package com.ipsdn_opt.app.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "ospfpathset")
public class OspfPathSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int pathser;
    private long sif_id;
    private long link_id; 

    public OspfPathSet() {
    }
    public OspfPathSet(long sif_id, int pathser, long link_id) {
        this.sif_id = sif_id;
        this.pathser = pathser;
        this.link_id = link_id;
    }
}
