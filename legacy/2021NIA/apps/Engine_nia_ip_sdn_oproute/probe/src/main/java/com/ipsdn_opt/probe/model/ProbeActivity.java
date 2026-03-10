package com.ipsdn_opt.probe.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "probeactivity")
public class ProbeActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDateTime measureddatetime;
    private long probe_id;
    public ProbeActivity() {
    }
    public ProbeActivity(long probe_id, LocalDateTime measureDateTime) {
        this.probe_id = probe_id;
        this.measureddatetime = measureDateTime;
    }
}
