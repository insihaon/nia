package com.ipsdn_opt.app.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "optrunhis")
public class OptRunHis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String optsource;
    private float cpulimit;
    private float memlimit;
    private float lostlimit;
    private LocalDate sourcedate;
    private LocalDateTime rundatetime;

    public OptRunHis() {
    }
    public OptRunHis(LocalDate sourcedate, String optsource, float cpulimit, float memlimit, Integer lostlimit) {
        this.optsource = optsource;
        this.cpulimit = cpulimit;
        this.memlimit = memlimit;
        this.lostlimit = lostlimit;
        this.rundatetime = LocalDateTime.now();
        this.sourcedate = sourcedate;
    }
}
