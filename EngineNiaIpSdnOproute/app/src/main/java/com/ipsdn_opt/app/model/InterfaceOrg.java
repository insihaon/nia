package com.ipsdn_opt.app.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "interface")
public class InterfaceOrg {
    @Id
    private long id;
    private String ifname;
    private long node_id;
    private String ipaddr;
    private Long hwif_id;
    private boolean usaged;
}
