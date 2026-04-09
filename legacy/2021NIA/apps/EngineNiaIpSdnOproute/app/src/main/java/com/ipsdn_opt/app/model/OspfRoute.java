package com.ipsdn_opt.app.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "ospfroute")
public class OspfRoute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long e2enode_id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "routepathset_id", nullable = false)
    private RoutePathSet routePathSet;
    
    // @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    // @JoinColumn(name = "ospfpath_id", nullable = false)
    // private List<OspfPathSet> ospfpathsets = new ArrayList<>();

    public OspfRoute() {
    }
    public OspfRoute(long e2enode_id) {
        this.e2enode_id = e2enode_id;
    }
}
