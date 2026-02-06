package com.ipsdn_opt.app.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "ospfroute")
public class OspfRouteOnly {
    @Id
    private long id;
    private long e2enode_id;
    private long routepathset_id;
    private LocalDateTime createddatetime;
}
