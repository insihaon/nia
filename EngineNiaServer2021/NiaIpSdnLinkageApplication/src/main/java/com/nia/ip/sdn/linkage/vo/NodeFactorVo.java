package com.nia.ip.sdn.linkage.vo;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

@Component
@Scope(value = "prototype")
@Data
public class NodeFactorVo implements Serializable {
    private int id;
    private float cpuUsage;
    private float memUsage;
    private int nodeId;
    private Timestamp measuredDateTime;
}
