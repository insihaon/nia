package com.ipsdn_opt.app.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class LinkInfo {
    @Id
    private long id;
    private long snode_id;
    private String snodename;
    private Long vsif_id;
    private String vsifname;
    private long sif_id;
    private String sifname;
    private long rnode_id;
    private String rnodename;
    private long rif_id;
    private String rifname;
    private Long send_strresid;
    private Long send_strifid;
    private Long receive_strresid;
    private Long receive_strifid;
}
