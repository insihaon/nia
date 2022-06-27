package com.nia.engine.vo;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Scope(value = "prototype")
@Data
public class BackboneLinkVo implements Serializable {
    private String srcNodeNum;
    private String srcNodeId;
    private String srcIfNum;
    private String srcIfId;
    private String destNodeNum;
    private String destNodeId;
    private String destIfNum;
    private String destIfId;
    private String linkDesc;
    private String vlan;
    private String tag;
    private String srcIpAddr;
    private String srcMacAddr;
    private String destIpAddr;
    private String destMacAddr;
    private String prefix;
    private String topology;
    private String bandwidth;
    private String linkType;
    private String linkDiv;
    private String circuitNum;
}
