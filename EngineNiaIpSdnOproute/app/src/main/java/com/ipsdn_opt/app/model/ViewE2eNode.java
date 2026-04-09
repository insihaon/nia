package com.ipsdn_opt.app.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name = "e2enode")
public class ViewE2eNode {
    @Id
    @JsonIgnore
    private long id;
    @JsonIgnore
    private long snode_id;
    @Transient
    private String source_node;
    @JsonIgnore
    private long rnode_id;
    @Transient
    private String destination_node;

}
