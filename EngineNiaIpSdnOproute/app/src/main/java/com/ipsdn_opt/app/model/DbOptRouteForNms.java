package com.ipsdn_opt.app.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class DbOptRouteForNms {
    @Id
    private long id;
    private long e2enode_id;
    private long routepathset_id;
    private Integer count;
    private Boolean usaged;
    private Integer pathser;
    private long link_id;
    private long send_node_id;
    private String send_node_name;
    private long send_interface_id;
    private String send_interface_name;
    private long receive_node_id;
    private String receive_node_name;
    private long receive_interface_id;
    private String receive_interface_name;
    private Long send_strresid;
    private Long send_strifid;
    private Long receive_strresid;
    private Long receive_strifid;
}
