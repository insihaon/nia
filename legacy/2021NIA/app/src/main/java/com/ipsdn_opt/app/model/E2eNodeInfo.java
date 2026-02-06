package com.ipsdn_opt.app.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class E2eNodeInfo {
    @Id
    private long id;
    private long snode_id;
    private String snodename;
    private long rnode_id;
    private String rnodename;
}
