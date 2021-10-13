package com.nia.data.linkage.ai.vo.ip.equip;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Component
@Scope(value = "prototype")
public class IpPortMstVo implements Serializable {
    private String nodeNum;
    private String nodeId;
    private String ifNum;
    private String ifId;
    private String ifNm;
    private int ifIndex;
    private String ifDesc;
    private String ifType;
    private String ifSpeed;
    private String ipAddr;
    private String macAddr;
}
