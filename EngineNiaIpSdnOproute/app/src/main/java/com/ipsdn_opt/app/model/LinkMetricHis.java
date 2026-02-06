package com.ipsdn_opt.app.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "linkmetrichis")
public class LinkMetricHis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long link_id;
    private Integer orgmetric;
    private Integer modifymetric;

    public LinkMetricHis() {
    }
    public LinkMetricHis(long link_id, Integer orgmetric, Integer modifymetric) {
        this.link_id = link_id;
        this.orgmetric = orgmetric;
        this.modifymetric = modifymetric;
    }
}
