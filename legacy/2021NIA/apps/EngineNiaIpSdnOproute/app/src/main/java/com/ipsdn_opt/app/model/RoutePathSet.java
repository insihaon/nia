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
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "routepathset")
public class RoutePathSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "routepathset_id", nullable = false)
    @OrderBy("pathser ASC")
    private List<RoutePath> routePaths = new ArrayList<>();

    public RoutePathSet() {
    }
    public RoutePathSet(List<RoutePath> routePaths) {
        this.routePaths = routePaths;
    }
}
