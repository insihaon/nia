package com.ipsdn_opt.app.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
public class DbNodeFactorForNms {
    @Id
    @JsonIgnore
    private long id;
    private Float cpuusage;
    private Float memusage;
    private long node_id;
    private Long strresid;
    private LocalDateTime measureddatetime;
    private Long timestamp;
}
