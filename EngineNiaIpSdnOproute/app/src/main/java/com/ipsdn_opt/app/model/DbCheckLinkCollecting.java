package com.ipsdn_opt.app.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class DbCheckLinkCollecting {
    @Id
    private long id;
    private long link_id;
    private String link_name;
    private LocalDateTime measureddatetime;
    private Double latency;
    private Double jitter;
    private Integer lost;
}
