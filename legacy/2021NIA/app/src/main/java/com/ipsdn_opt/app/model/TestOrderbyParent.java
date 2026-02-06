package com.ipsdn_opt.app.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "node")
public class TestOrderbyParent {
    @Id
    private long id;
    private String nodename;
    private String loopbackaddr;
    private String mgmtaddr;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "node_id")
    @OrderBy("ipaddr ASC")
    private List<TestOrderbyChild> interfaces = new ArrayList<>();
}
