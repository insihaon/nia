package com.ipsdn_opt.app.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class DbCheckNodeCollecting {
    @Id
    private long id;
    private long node_id;
    private String nodename;
    private LocalDateTime measureddatetime;
    private Float cpuusage;
    private Float memusage;
}
