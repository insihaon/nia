package com.ipsdn_opt.app.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

@Data
@Entity
@Table(name = "e2enode")
public class E2eNode {
    @Id
    private long id;
    private long snode_id;
    private long rnode_id;
    private boolean usaged;
}
