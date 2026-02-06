package com.ipsdn_opt.app.model;

import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "linktraffic")
public class LinkTraffic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long if_id;
    private String nms_interface_name;
    private Integer txbitrate;
    private Integer rxbitrate;
    private Integer txpktrate;
    private Integer rxpktrate;
    private LocalDateTime measureddatetime;

    public LinkTraffic() {
    }
    public LinkTraffic(long if_id, Integer txbitrate, Integer rxbitrate, Integer txpktrate, Integer rxpktrate, LocalDateTime measureddatetime) {
        this.if_id = if_id;
        this.txbitrate = txbitrate;
        this.rxbitrate = rxbitrate;
        this.txpktrate = txpktrate;
        this.rxpktrate = rxpktrate;
        this.measureddatetime = measureddatetime;
    }
}
