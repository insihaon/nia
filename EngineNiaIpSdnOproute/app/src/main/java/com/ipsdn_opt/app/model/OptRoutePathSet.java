package com.ipsdn_opt.app.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "optroutepathset")
public class OptRoutePathSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int pathser;
    private long link_id;

    public OptRoutePathSet() {
    }
    public OptRoutePathSet(int pathser, long link_id) {
        this.pathser = pathser;
        this.link_id = link_id;
    }
}
