package com.ipsdn_opt.probe.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "link")
public class LinkNode {
    @Id
    private long id;
    private long snode_id;
    private String sifname;
    private long rnode_id;
    private String rifname;
}
