package com.ipsdn_opt.app.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "linkfactor")
public class LinkFactor {
    @Id
    private long id;
    private long link_id;
    private Double latency;
    private Double jitter;
    private Integer lost;
    private Integer hops;
    private Boolean corrected;
    private LocalDateTime measureddatetime;
}
