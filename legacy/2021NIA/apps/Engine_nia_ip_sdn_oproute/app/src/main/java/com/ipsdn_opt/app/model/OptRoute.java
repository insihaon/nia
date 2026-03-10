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
@Table(name = "optroute")
public class OptRoute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long runhis_id;
    private long e2enode_id;
    private Boolean usaged = false;
    //private Long routepathset_id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "routepathset_id", nullable = false)
    private RoutePathSet routePathSet;
    // private List<RoutePath> routePathSets = new ArrayList<>();

    public OptRoute() {
    }
    public OptRoute(long runhis_id, long e2enode_id) {
        this.runhis_id = runhis_id;
        this.e2enode_id = e2enode_id;
    }
}
