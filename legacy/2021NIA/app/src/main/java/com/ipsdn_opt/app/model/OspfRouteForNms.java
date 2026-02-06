package com.ipsdn_opt.app.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name = "ospfroute")
public class OspfRouteForNms {
    @Id
    @JsonIgnore
    private long id;
    private long e2enode_id;
    private long routepathset_id;
    @Transient
    List<RoutePathForNms> current_paths = new ArrayList<>();

    public OspfRouteForNms() {
    }
    public OspfRouteForNms(long e2enode_id) {
        this.e2enode_id = e2enode_id;
    }
    
}
