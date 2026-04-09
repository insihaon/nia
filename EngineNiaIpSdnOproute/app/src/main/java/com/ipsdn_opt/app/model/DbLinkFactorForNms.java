package com.ipsdn_opt.app.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
public class DbLinkFactorForNms {
    @Id
    @JsonIgnore
    private long id;
    private Double latency;
    private Double jitter;
    private Integer lost;
    private long link_id;
    private long send_node_id;
    private long send_interface_id;
    private long receive_node_id;
    private long receive_interface_id;
    private Long send_strresid;
    private Long send_strifid;
    private Long receive_strresid;
    private Long receive_strifid;
    private LocalDateTime measureddatetime;
    private Long timestamp;
}
