package com.ipsdn_opt.app.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
public class RoutePathForNms implements Comparable<RoutePathForNms> {
    @Id
    @JsonIgnore
    private long id;
    private Integer pathser;
    private long link_id;
    @JsonIgnore
    private long routepathset_id;
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
    
    @JsonIgnore
    @Transient
    private String paths;
    public RoutePathForNms() {
    }
    public RoutePathForNms(Integer pathser, long link_id, long send_node_id, String send_node_name, long send_interface_id, String send_interface_name,
            long receive_node_id, String receive_node_name, long receive_interface_id, String receive_interface_name, Long send_strresid, Long send_strifid, Long receive_strresid, Long receive_strifid) {
        this.pathser = pathser;
        this.link_id = link_id;
        this.send_node_id = send_node_id;
        this.send_node_name = send_node_name;
        this.send_interface_id = send_interface_id;
        this.send_interface_name = send_interface_name;
        this.receive_node_id = receive_node_id;
        this.receive_node_name = receive_node_name;
        this.receive_interface_id = receive_interface_id;
        this.receive_interface_name = receive_interface_name;
        this.send_strresid = send_strresid;
        this.send_strifid = send_strifid;
        this.receive_strresid = receive_strresid;
        this.receive_strifid = receive_strifid;
        this.paths = send_node_name + " -> " + receive_node_name;
    }

    @Override
    public int compareTo(RoutePathForNms routePath) {
        if(routePath.pathser < pathser) return 1;
        else if(routePath.pathser > pathser) return -1;
        return 0;
    }
}
