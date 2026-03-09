package com.ipsdn_opt.app.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class DbChangedMetric {
    @Id
    private long id;
    private long runhis_id;
    private long snode_id;
    private Long vif_id;
    private String vifname;
    private long sif_id;
    private String sifname;
    private long link_id;
    private Integer orgmetric;
    private Integer modifymetric;
}
