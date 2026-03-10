package com.ipsdn_opt.app.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class CpuUsage {
    @Id
    private long id;
    private long node_id;
    private String nodename;
    private Float cpuusage;
    private LocalDate sourcedate;
}
