package com.ipsdn_opt.app.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name = "nodefactor")
public class NodeFactor {
    @Id
    @JsonIgnore
    private long id;
    @JsonIgnore
    private long node_id;
    private Float cpuusage;
    private Float memusage;
    private LocalDateTime measureddatetime;
}
