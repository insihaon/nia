package com.nia.engine.vo.sdn.factor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Component()
@Scope(value = "prototype")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NodeFactorInfoVo implements Serializable {
    private String strresid;
    private float cpuUsage;
    private float memUsage;
    private Timestamp measuredDatetime;
    private String nodeId;
}
