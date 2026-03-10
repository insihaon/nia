package com.ipsdn_opt.app.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "settings")
public class Settings {
    @Id
    private long id;
    private Boolean automeasurement;
    private Long measurementperiod;
    private String optsource;
    private Integer avgrange;
    private Integer medposition;
    private float latencylimit;
    private float jitterlimit;
    private Integer trafficlimit;
    private float cpulimit;
    private float memlimit;
    private Integer lostlimit;
    private float latencyrefrate;
    private float jitterrefrate;
    private float trafficrefrate;
    private float metriclimit;
    private Boolean configmetric;
}
