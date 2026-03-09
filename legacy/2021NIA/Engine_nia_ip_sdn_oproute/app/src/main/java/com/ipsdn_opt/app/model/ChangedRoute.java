package com.ipsdn_opt.app.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class ChangedRoute {
    @Id
    private long id;
    private long e2enode_id;
    private long snode_id;
    private String snodename;
    private long rnode_id;
    private String rnodename;
    private long ospf_routepathset_id;
    private long opt_routepathset_id;
    private Integer optcnt;
}
