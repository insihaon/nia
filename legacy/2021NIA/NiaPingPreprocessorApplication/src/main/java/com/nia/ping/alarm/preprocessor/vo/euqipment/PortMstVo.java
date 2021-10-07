package com.nia.ping.alarm.preprocessor.vo.euqipment;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Component
@Scope(value = "prototype")
public class PortMstVo implements Serializable {
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
