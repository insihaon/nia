package com.ipsdn_opt.app.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
public class DbLinkTrafficForNms {
    @Id
    @JsonIgnore
    private long id;
    private long node_id;
    private Long strresid;
    private long interface_id;
    private Long strifid;
    private Long fltbpsout;
    private Long fltbpsin;
    private Long fltppsout;
    private Long fltppsin;
    private LocalDateTime measureddatetime;
    private Long timestamp;
}
