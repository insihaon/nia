package com.nia.data.linkage.vo.topology;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Component()
@Scope(value = "prototype")
public class ServicePceVo implements Serializable {
    private String serviceId;
    private int cost;
    private int pceIndex;
    private String dstNodeId;
    private String srcTpId;
    private String dstTpId;
    private String srcNodeId;
}
