package com.ipsdn_opt.app.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "link")
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Long vsif_id; // VLAN I/F
    private long sif_id;  // HW Interface
    private long rif_id;
    private String speed;
    private Integer ospfmetric;
    private double latencystd;
    private boolean usaged;

    public Link() {
    }
    public Link(long sif_id, long rif_id, Integer ospfmetric, boolean usaged) {
        this.sif_id = sif_id;
        this.rif_id = rif_id;
        this.ospfmetric = ospfmetric;
        this.usaged = usaged;
    }
}
