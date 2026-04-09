package com.ipsdn_opt.app.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "ospfdbrouter")
public class OspfDbRouter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String linkstateid;
    private String nrouteraddr;
    private String ifnetaddr;
    private int tos0metric;

    public OspfDbRouter() {
    }
    public OspfDbRouter(String linkstateid, String nrouteraddr) {
        this.linkstateid = linkstateid;
        this.nrouteraddr = nrouteraddr;
    }
}
