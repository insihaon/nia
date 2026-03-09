package com.ipsdn_opt.app.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class DbCheckTrafficCollecting {
    @Id
    private long id;
    private long node_id;
    private String nodename;
    private long if_id;
    private String ifname;
    private LocalDateTime measureddatetime;
    private Integer txbitrate;
    private Integer rxbitrate;
    private Integer txpktrate;
    private Integer rxpktrate;
}
