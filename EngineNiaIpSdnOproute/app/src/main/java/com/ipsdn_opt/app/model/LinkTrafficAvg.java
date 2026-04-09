package com.ipsdn_opt.app.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "linktrafficavg")
public class LinkTrafficAvg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long if_id;
    private Integer txbitrate_avg;
    private Integer txbitrate_med;
    private Integer txpktrate_avg;
    private Integer txpktrate_med;
    private Integer avgrange;
    private Integer medposition;
    private LocalDate sourcedate;

    public LinkTrafficAvg() {
    }
    public LinkTrafficAvg(long if_id) {
        this.if_id = if_id;
    }
}
