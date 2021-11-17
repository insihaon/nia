package com.nia.data.linkage.ip.perf.vo.equip;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Scope(value = "prototype")
@Data
public class NodeMstVo implements Serializable {
    private String nodeNum;
    private String nodeId;
    private String nodeNm;
    private String modelNum;
    private String modelId;
    private String ipAddr;
}
