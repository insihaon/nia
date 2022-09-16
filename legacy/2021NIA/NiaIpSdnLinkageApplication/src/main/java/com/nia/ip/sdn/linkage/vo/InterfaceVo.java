package com.nia.ip.sdn.linkage.vo;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Scope(value = "prototype")
@Data
public class InterfaceVo implements Serializable {
    private int id;
    private String ifName;
    private int nodeId;
    private String ipAddr;
    private int hwIfId;

}
